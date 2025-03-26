package de.osca.fama

import de.osca.fama.settings.Settings
import de.osca.fama.smarthomeadapter.SmartHomeAdapter

data class MockedSettings(
    override val debug: Boolean = true,
    override val enableSentry: Boolean = false,
    override val smartHomeType: SmartHomeAdapter.Type = SmartHomeAdapter.Type.IB,
    override val enableSensorStation: Boolean = true,
    override val mqttHost: String? = null,
    override val mqttPort: Int = 1883,
    override val mqttTlsEnabled: Boolean = false,
    override val mqttClientId: String = "fama",
    override val mqttUsername: String? = "fama-username",
    override val mqttPassword: String? = "fama-password",
    override val homeAssistantDiscoveryPrefix: String = "homeassistant",
    override val ioBrokerUrl: String = "http://localhost:8087/v1/",
    override val ioBrokerPrefix: String = "fama",
    override val ioBrokerStationFolderPrefix: String = "sensor-station",
    override val sensorStationFilterIds: String = "some-filter-id1, some-filter-id2",
) : Settings
