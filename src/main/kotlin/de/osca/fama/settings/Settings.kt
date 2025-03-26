package de.osca.fama.settings

import de.osca.fama.smarthomeadapter.SmartHomeAdapter

interface Settings {
    // General
    val debug: Boolean

    val enableSentry: Boolean
    val smartHomeType: SmartHomeAdapter.Type

    // Devices
    val enableSensorStation: Boolean

    // MQTT
    val mqttHost: String?
    val mqttPort: Int
    val mqttTlsEnabled: Boolean
    val mqttClientId: String
    val mqttUsername: String?
    val mqttPassword: String?

    // Home Assistant
    val homeAssistantDiscoveryPrefix: String

    // IoBroker
    val ioBrokerUrl: String
    val ioBrokerPrefix: String
    val ioBrokerStationFolderPrefix: String

    // Station Subscriptions
    val sensorStationFilterIds: String
}
