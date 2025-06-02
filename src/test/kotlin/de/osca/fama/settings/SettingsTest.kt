package de.osca.fama.settings

import de.osca.fama.MockedSettingsImpl
import de.osca.fama.smarthomeadapter.SmartHomeAdapter
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.lang.reflect.Field
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsTest {
    private val envMap: MutableMap<String, String> by lazy {
        val classOfMap: Class<*> = System.getenv().javaClass
        val field: Field = classOfMap.getDeclaredField("m")
        field.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return@lazy field.get(System.getenv()) as MutableMap<String, String>
    }

    @AfterEach
    fun tearDown() {
        envMap.clear()
    }

    @Test
    fun `should throw EnvVarMissingException when ioBrokerUrl is missing`() {
        assertFailsWith<EnvVarMissingException> {
            MockedSettingsImpl().envVarMissingExceptionTestVariable
        }
    }

    @Test
    fun `should enable debug mode when DEBUG is true`() {
        envMap["DEBUG"] = "true"
        val settings = SettingsImpl()
        assertTrue(settings.debug)
    }

    @Test
    fun `should disable Sentry when ENABLE_SENTRY is false`() {
        envMap["ENABLE_SENTRY"] = "false"
        val settings = SettingsImpl()
        assertFalse(settings.enableSentry)
    }

    @Test
    fun `should set smart home type to IB when SMART_HOME_TYPE is IB`() {
        envMap["SMART_HOME_TYPE"] = "IB"
        val settings = SettingsImpl()
        assertEquals(SmartHomeAdapter.Type.IB, settings.smartHomeType)
    }

    @Test
    fun `should enable sensor station when ENABLE_SENSOR_STATION is true`() {
        envMap["ENABLE_SENSOR_STATION"] = "true"
        val settings = SettingsImpl()
        assertTrue(settings.enableSensorStation)
    }

    @Test
    fun `should set MQTT host to localhost when MQTT_HOST is localhost`() {
        envMap["MQTT_HOST"] = "localhost"
        val settings = SettingsImpl()
        assertEquals("localhost", settings.mqttHost)
    }

    @Test
    fun `should set MQTT port to 1883 when MQTT_PORT is 1883`() {
        envMap["MQTT_PORT"] = "1883"
        val settings = SettingsImpl()
        assertEquals(1883, settings.mqttPort)
    }

    @Test
    fun `should set MQTT port to default value when MQTT_PORT is not set`() {
        val settings = SettingsImpl()
        assertEquals(1883, settings.mqttPort)
    }

    @Test
    fun `should disable MQTT TLS when MQTT_TLS_ENABLED is false`() {
        envMap["MQTT_TLS_ENABLED"] = "false"
        val settings = SettingsImpl()
        assertFalse(settings.mqttTlsEnabled)
    }

    @Test
    fun `should set MQTT client ID to fama when MQTT_CLIENT_ID is fama`() {
        envMap["MQTT_CLIENT_ID"] = "fama"
        val settings = SettingsImpl()
        assertEquals("fama", settings.mqttClientId)
    }

    @Test
    fun `should set MQTT username to user when MQTT_USERNAME is user`() {
        envMap["MQTT_USERNAME"] = "user"
        val settings = SettingsImpl()
        assertEquals("user", settings.mqttUsername)
    }

    @Test
    fun `should set MQTT password to password when MQTT_PASSWORD is password`() {
        envMap["MQTT_PASSWORD"] = "password"
        val settings = SettingsImpl()
        assertEquals("password", settings.mqttPassword)
    }

    @Test
    fun `should set Home Assistant discovery prefix to homeassistant when HOME_ASSISTANT_DISCOVERY_PREFIX is homeassistant`() {
        envMap["HOME_ASSISTANT_DISCOVERY_PREFIX"] = "homeassistant"
        val settings = SettingsImpl()
        assertEquals("homeassistant", settings.homeAssistantDiscoveryPrefix)
    }

    @Test
    fun `should set ioBroker URL when IO_BROKER_URL is set`() {
        envMap["IO_BROKER_URL"] = "http://localhost:8087/v1/"
        val settings = SettingsImpl()
        assertEquals("http://localhost:8087/v1/", settings.ioBrokerUrl)
    }

    @Test
    fun `should set ioBroker prefix to fama when IO_BROKER_PREFIX is fama`() {
        envMap["IO_BROKER_PREFIX"] = "fama"
        val settings = SettingsImpl()
        assertEquals("fama", settings.ioBrokerPrefix)
    }

    @Test
    fun `should set ioBroker station folder prefix to sensor-station when IO_BROKER_STATION_FOLDER_PREFIX is sensor-station`() {
        envMap["IO_BROKER_STATION_FOLDER_PREFIX"] = "sensor-station"
        val settings = SettingsImpl()
        assertEquals("sensor-station", settings.ioBrokerStationFolderPrefix)
    }
}
