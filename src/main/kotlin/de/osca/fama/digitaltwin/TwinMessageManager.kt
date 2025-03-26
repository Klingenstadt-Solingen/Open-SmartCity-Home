package de.osca.fama.digitaltwin

import de.osca.fama.digitaltwin.model.sensor.Sensor
import de.osca.fama.logger.logger
import de.osca.fama.settings.BuildConfig
import de.osca.fama.settings.Settings
import de.osca.fama.smarthomeadapter.SmartHomeAdapter
import io.ktor.client.HttpClient
import io.sentry.kotlin.SentryContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.config.HeartBeat
import org.hildan.krossbow.stomp.config.HeartBeatTolerance
import org.hildan.krossbow.stomp.config.StompConfig
import org.hildan.krossbow.stomp.headers.AckMode
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import org.hildan.krossbow.websocket.reconnection.withAutoReconnect
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

class TwinMessageManager : KoinComponent {
    private val settings: Settings by inject()
    private val logger by logger()
    private val buildConfig: BuildConfig by inject()
    private val smartHomeAdapter: SmartHomeAdapter by inject()
    private val httpClient: HttpClient by inject()
    private var session: StompSession? = null
    private val dispatcher: CoroutineContext = Dispatchers.IO + SentryContext()

    private val subscriptions = mutableListOf<Job>()

    private val stompConfig =
        StompConfig().apply {
            connectionTimeout = 15.seconds
            heartBeat =
                HeartBeat(
                    minSendPeriod = 30.seconds,
                    expectedPeriod = 100.seconds,
                )
            heartBeatTolerance =
                HeartBeatTolerance(
                    incomingMargin = 20.seconds,
                    outgoingMargin = 20.seconds,
                )
        }

    private val oneHourTimeStampFromNow by lazy {
        Clock.System
            .now()
            .minus(1.hours)
            .epochSeconds
            .toString()
    }

    suspend fun start() {
        logger.i { "Stomp Client Start Connecting to Server" }
        session =
            StompClient(
                KtorWebSocketClient(httpClient = httpClient).withAutoReconnect(),
                stompConfig,
            ).connect(
                buildConfig.rabbitmqStompUrl,
                buildConfig.rabbitmqStompUsername,
                buildConfig.rabbitmqStompPassword,
                "/",
            )
        logger.i { "Stomp Client Connected to Server" }
    }

    suspend fun stop() {
        subscriptions.forEach { it.cancelAndJoin() }
        session?.disconnect()
    }

    suspend fun listenSensors() =
        withContext(dispatcher) {
            subscriptions.add(
                launch {
                    logger.i { "Start Subscribing to Sensors" }
                    val header =
                        StompSubscribeHeaders(
                            "/amq/queue/s.public.sensor",
                        ) {
                            ack = AckMode.CLIENT
                            set("prefetch-count", "50")
                            set("x-stream-offset", oneHourTimeStampFromNow)
                        }

                    val subscription =
                        session?.subscribeAndAutoAck<DigitalTwinMessage<Sensor>>(
                            header,
                        )
                    logger.i("Subscribed to Sensors")
                    subscription?.collect { message ->
                        message?.let { digitalTwinMessage ->
                            if (!checkVersions(digitalTwinMessage)) return@collect
                            if (settings.sensorStationFilterIds.isNotBlank()) {
                                if (!settings.sensorStationFilterIds.split(",").contains(digitalTwinMessage.payload.station.objectId)) {
                                    return@collect
                                }
                            }
                            smartHomeAdapter.updateSensorStation(digitalTwinMessage.payload)
                        }
                    }
                },
            )
        }

    private fun checkVersions(
        message: DigitalTwinMessage<*>,
        schemaVersion: Int = 1,
        messageVersion: Int = 1,
    ): Boolean = message.schemaVersion == schemaVersion && message.messageVersion == messageVersion
}

val json =
    Json {
        ignoreUnknownKeys = true
        explicitNulls = true
    }

private suspend inline fun <reified T : Any> StompSession.subscribeAndAutoAck(header: StompSubscribeHeaders): Flow<T?> {
    return subscribe(header).map { message ->
        try {
            val entity = json.decodeFromString<T>(message.bodyAsText)
            message.headers.ack?.let { ack(it) }
            return@map entity
        } catch (e: SerializationException) {
            return@map null
        }
    }
}
