package de.osca.fama.smarthomeadapter.homeassistant

import de.osca.fama.TestFixture
import de.osca.fama.mockModules
import de.osca.fama.settings.BuildConfig
import de.osca.fama.smarthomeadapter.HomeAssistantAdapter
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import kotlin.test.assertContains
import kotlin.test.assertEquals

class HomeAssistantAdapterTest : KoinTest {
    private val testSensor = TestFixture.sensor
    private val mqttManager: HomeAssistantMockMqttManager by inject()
    private val buildConfig: BuildConfig by inject()
    private val homeAssistantAdapter: HomeAssistantAdapter = HomeAssistantAdapter()

    @JvmField
    @RegisterExtension
    val koinTestExtension =
        KoinTestExtension.create {
            modules(
                mockModules,
            )
        }

    @BeforeEach
    fun setUp() {
        mqttManager.messages.clear()
        homeAssistantAdapter.configuredComponents.clear()
    }

    @Test
    fun testSensorCreation() =
        runTest {
            val homeAssistantPayload =
                HomeAssistantPayload(
                    uniqueId = testSensor.objectId,
                    name = testSensor.sensorType.name,
                    icon = null,
                    unitOfMeasurement = testSensor.sensorType.unit,
                    deviceClass =
                        HomeAssistantDeviceClass.entries.find {
                            it.name == testSensor.sensorType.type.name
                        },
                    stateTopic =
                        homeAssistantAdapter.topic(
                            HomeAssistantComponent.SENSOR,
                            testSensor.objectId,
                            HomeAssistantTopicType.STATE,
                        ),
                    device =
                        HomeAssistantDevice(
                            identifiers = testSensor.station.objectId,
                            name = testSensor.station.name,
                            swVersion = buildConfig.version,
                            configurationUrl = buildConfig.supportUrl,
                            model = "Sensor Station",
                        ),
                    origin =
                        HomeAssistantOrigin(
                            name = "Open SmartCity Home",
                            swVersion = buildConfig.version,
                            supportUrl = buildConfig.supportUrl,
                        ),
                )

            homeAssistantAdapter.updateSensorStation(testSensor)
            assertContains(homeAssistantAdapter.configuredComponents, testSensor.objectId)
            assertEquals(homeAssistantPayload, json.decodeFromString<HomeAssistantPayload>(mqttManager.messages[0].second))
            assertEquals(2, mqttManager.messages.size)
            assertEquals(
                homeAssistantAdapter.topic(
                    HomeAssistantComponent.SENSOR,
                    testSensor.objectId,
                    HomeAssistantTopicType.CONFIG,
                ),
                mqttManager.messages[0].first,
            )
        }

    @Test
    fun testSensorStateCreation() =
        runTest {
            homeAssistantAdapter.updateSensorStation(testSensor)
            assertEquals(
                mqttManager.messages[1].first,
                homeAssistantAdapter.topic(
                    HomeAssistantComponent.SENSOR,
                    testSensor.objectId,
                    HomeAssistantTopicType.STATE,
                ),
            )
            assertEquals(testSensor.value.toString(), mqttManager.messages[1].second)
        }

    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        private val json =
            Json {
                ignoreUnknownKeys = true
                namingStrategy = JsonNamingStrategy.SnakeCase
                explicitNulls = false
            }
    }
}
