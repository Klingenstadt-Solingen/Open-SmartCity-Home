package de.osca.fama.settings

import de.osca.fama.smarthomeadapter.SmartHomeAdapter

class SettingsImpl : Settings {
    override val debug by envBoolean("DEBUG")
    override val enableSentry by envBoolean("ENABLE_SENTRY")
    override val smartHomeType by envEnum<SmartHomeAdapter.Type>("SMART_HOME_TYPE")
    override val enableSensorStation by envBoolean("ENABLE_SENSOR_STATION", defaultValue = true)
    override val mqttHost: String? by envString("MQTT_HOST")
    override val mqttPort: Int by envInt("MQTT_PORT", defaultValue = 1883)
    override val mqttTlsEnabled: Boolean by envBoolean("MQTT_TLS_ENABLED")
    override val mqttClientId: String by envString("MQTT_CLIENT_ID", defaultValue = "opensmartcityhome")
    override val mqttUsername: String? by envString("MQTT_USERNAME")
    override val mqttPassword: String? by envString("MQTT_PASSWORD")
    override val homeAssistantDiscoveryPrefix: String by envString("HOME_ASSISTANT_DISCOVERY_PREFIX", defaultValue = "homeassistant")
    override val ioBrokerUrl: String by envString("IO_BROKER_URL")
    override val ioBrokerPrefix: String by envString("IO_BROKER_PREFIX", defaultValue = "opensmartcityhome")
    override val ioBrokerStationFolderPrefix: String by envString("IO_BROKER_STATION_FOLDER_PREFIX", defaultValue = "sensor-station")
    override val sensorStationFilterIds: String by envString("SENSOR_STATION_FILTER_IDS", defaultValue = "")
}
