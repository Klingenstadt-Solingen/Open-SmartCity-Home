package de.osca.fama

import de.osca.fama.settings.Settings
import de.osca.fama.settings.envBoolean
import de.osca.fama.settings.envEnum
import de.osca.fama.settings.envInt
import de.osca.fama.settings.envString
import de.osca.fama.smarthomeadapter.SmartHomeAdapter

class MockedSettingsImpl : Settings {
    override val debug by envBoolean("DEBUG")
    override val enableSentry by envBoolean("ENABLE_SENTRY")
    override val smartHomeType by envEnum<SmartHomeAdapter.Type>("SMART_HOME_TYPE")
    override val enableSensorStation by envBoolean("ENABLE_SENSOR_STATION", defaultValue = true)
    override val mqttHost: String? by envString("MQTT_HOST")
    override val mqttPort: Int by envInt("MQTT_PORT", defaultValue = 1883)
    override val mqttTlsEnabled: Boolean by envBoolean("MQTT_TLS_ENABLED")
    override val mqttClientId: String by envString("MQTT_CLIENT_ID", defaultValue = "fama")
    override val mqttUsername: String? by envString("MQTT_USERNAME")
    override val mqttPassword: String? by envString("MQTT_PASSWORD")
    override val homeAssistantDiscoveryPrefix: String by envString("HOME_ASSISTANT_DISCOVERY_PREFIX", defaultValue = "homeassistant")
    override val ioBrokerUrl: String by envString("IO_BROKER_URL")
    override val ioBrokerPrefix: String by envString("IO_BROKER_PREFIX", defaultValue = "fama")
    override val ioBrokerStationFolderPrefix: String by envString("IO_BROKER_STATION_FOLDER_PREFIX", defaultValue = "sensor-station")
    override val sensorStationFilterIds: String by envString("SENSOR_STATION_FILTER_IDS", defaultValue = "")
    val envVarMissingExceptionTestVariable: String by envString("ENV_VAR_MISSING_EXCEPTION_TEST_VARIABLE")
}
