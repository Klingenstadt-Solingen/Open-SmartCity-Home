package de.osca.fama.smarthomeadapter

import de.osca.fama.digitaltwin.model.sensor.Sensor
import de.osca.fama.logger.logger
import de.osca.fama.settings.Settings
import de.osca.fama.smarthomeadapter.iobroker.IoBrokerApiCommandType
import de.osca.fama.smarthomeadapter.iobroker.IoBrokerCommonType
import de.osca.fama.smarthomeadapter.iobroker.IoBrokerObjectPayload
import de.osca.fama.smarthomeadapter.iobroker.IoBrokerObjectType
import de.osca.fama.smarthomeadapter.iobroker.IoBrokerStatePayload
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.defaultForFileExtension
import io.ktor.http.isSuccess
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class IoBrokerAdapter :
    SmartHomeAdapter,
    KoinComponent {
    override val mqttEnabled: Boolean = false
    private val settings: Settings by inject()
    private val httpClient: HttpClient by inject()
    private val logger by logger()

    override suspend fun updateSensorStation(sensor: Sensor) {
        logger.d("Start Sending: ${sensor.sensorType.name}")
        ensureFolderStructure()
        ensureStation(sensor = sensor)
        ensureSensor(sensor = sensor)
        updateSensorValue(sensor = sensor)
    }

    private suspend fun ensureFolderStructure() {
        val url = restUrl(IoBrokerApiCommandType.OBJECT, settings.ioBrokerPrefix)
        if (!httpClient.get(url).status.isSuccess()) {
            val baseFolderPayload =
                IoBrokerObjectPayload.from(
                    name = settings.ioBrokerPrefix,
                    type = IoBrokerObjectType.FOLDER,
                    commonType = IoBrokerCommonType.FOLDER,
                )
            val baseFolderPayloadString = json.encodeToString(baseFolderPayload)
            httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(baseFolderPayloadString)
            }
        }
        if (!httpClient.get("$url.${settings.ioBrokerStationFolderPrefix}").status.isSuccess()) {
            val baseFolderPayload =
                IoBrokerObjectPayload.from(
                    name = settings.ioBrokerStationFolderPrefix,
                    type = IoBrokerObjectType.FOLDER,
                    commonType = IoBrokerCommonType.FOLDER,
                )
            val baseFolderPayloadString = json.encodeToString(baseFolderPayload)
            httpClient.post("$url.${settings.ioBrokerStationFolderPrefix}") {
                contentType(ContentType.Application.Json)
                setBody(baseFolderPayloadString)
            }
        }
    }

    private suspend fun ensureStation(sensor: Sensor) {
        val stationUrl =
            restUrl(
                IoBrokerApiCommandType.OBJECT,
                "${settings.ioBrokerPrefix}.${settings.ioBrokerStationFolderPrefix}.${sensor.station.objectId}",
            )
        val response = httpClient.get(stationUrl)
        val stationPayload =
            IoBrokerObjectPayload.from(
                name = sensor.station.name,
                type = IoBrokerObjectType.FOLDER,
                commonType = IoBrokerCommonType.FOLDER,
            )
        val stationPayloadString = json.encodeToString(stationPayload)
        if (response.status.isSuccess()) {
            val responsePayload = json.decodeFromString<IoBrokerObjectPayload>(response.bodyAsText())
            if (responsePayload.common.name != stationPayload.common.name) {
                httpClient.put(stationUrl) {
                    contentType(ContentType.Application.Json)
                    setBody(stationPayloadString)
                }
            }
        } else {
            httpClient.post(stationUrl) {
                contentType(ContentType.Application.Json)
                setBody(stationPayloadString)
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun ensureIcon(sensor: Sensor): String? {
        val icon = sensor.sensorType.icon
        if (icon != null) {
            val iconResponse = httpClient.get(icon)
            if (iconResponse.status.isSuccess()) {
                val iconBase64 = Base64.Default.encode(iconResponse.body<ByteArray>())
                val contentType = ContentType.defaultForFileExtension(icon)
                return "data:${contentType.contentType}/${contentType.contentSubtype};base64,$iconBase64"
            }
        }
        return null
    }

    private suspend fun ensureSensor(sensor: Sensor) {
        val sensorUrl =
            restUrl(
                IoBrokerApiCommandType.OBJECT,
                "${settings.ioBrokerPrefix}.${settings.ioBrokerStationFolderPrefix}.${sensor.station.objectId}.${sensor.objectId}",
            )
        val response = httpClient.get(sensorUrl)
        val icon = ensureIcon(sensor = sensor)
        val sensorPayload =
            IoBrokerObjectPayload.from(
                name = sensor.sensorType.name,
                unit = sensor.sensorType.unit,
                type = IoBrokerObjectType.STATE,
                commonType = IoBrokerCommonType.NUMBER,
                icon = icon,
            )
        val sensorPayloadString = json.encodeToString(sensorPayload)
        if (response.status.isSuccess()) {
            val responsePayload = json.decodeFromString<IoBrokerObjectPayload>(response.bodyAsText())
            if (responsePayload.common.name != sensorPayload.common.name || responsePayload.common.icon != sensorPayload.common.icon) {
                httpClient.put(sensorUrl) {
                    contentType(ContentType.Application.Json)
                    setBody(sensorPayloadString)
                }
            }
        } else {
            httpClient.post(sensorUrl) {
                contentType(ContentType.Application.Json)
                setBody(sensorPayloadString)
            }
        }
    }

    private suspend fun updateSensorValue(sensor: Sensor) {
        val sensorUrl =
            restUrl(
                IoBrokerApiCommandType.STATE,
                "${settings.ioBrokerPrefix}.${settings.ioBrokerStationFolderPrefix}.${sensor.station.objectId}.${sensor.objectId}",
            )
        val sensorStatePayload = IoBrokerStatePayload(value = sensor.value)
        val sensorStatePayloadString = json.encodeToString(sensorStatePayload)
        httpClient.patch(sensorUrl) {
            contentType(ContentType.Application.Json)
            setBody(sensorStatePayloadString)
        }
    }

    private fun restUrl(
        commandType: IoBrokerApiCommandType,
        suffix: String,
    ): String = "${settings.ioBrokerUrl}${commandType.name.lowercase()}/$suffix"

    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        val json =
            Json {
                namingStrategy = JsonNamingStrategy.SnakeCase
                explicitNulls = false
                ignoreUnknownKeys = true
            }
    }
}
