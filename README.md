
<div style="display:flex;gap:1%;margin-bottom:20px">
  <h1 style="border:none"> Fama - City of Things Adapter System</h1>
  <img height="100px" alt="logo" src="documentation/images/logo.svg">
</div>

 Fama is a software solution designed to bridge weather sensor data with home automation systems. Currently focused on the city of Solingen, Fama is designed to receive data from RabbitMQ, process it, and format it for seamless integration with various supported home systems.

 ## Features
 - **Weather Data Integration:** Connects to RabbitMQ to receive real-time weather sensor data.
 - **Home Automation Support:** Formats and forwards data to:
    - **Home Assistant** (including systems compatible with Home Assistant's format and autodiscovery, such as OpenHAB).
    - **ioBroker** via its API.
- **Dynamic Configuration:** Easily adaptable to other cities or systems in the future.

## Supported Systems

### Home Assistant and OpenHAB

Fama uses MQTT to publish weather sensor data in Home Assistant's autodiscovery format. This allows:

- Easy integration with Home Assistant.

- Compatibility with OpenHAB when configured to use Home Assistant-formatted data.

See the [guide on connecting Fama to Home Assistant.](/documentation/homesystem/HOMEASSISTANT.md) 

[![Open your Home Assistant instance and show the add add-on repository dialog with a specific repository URL pre-filled.](https://my.home-assistant.io/badges/supervisor_add_addon_repository.svg)](https://my.home-assistant.io/redirect/supervisor_add_addon_repository/?repository_url=https://github.com/nedeco/fama-hass-addons)

See the [guide on connecting Fama to OpenHAB](/documentation/homesystem/OPENHAB.md).

### ioBroker

Fama integrates with ioBroker via the REST-API Adapter, enabling direct communication for weather sensor data management.

See the [guide on connecting Fama to ioBroker](/documentation/homesystem/IOBROKER.md).

## Contributing

Contributions are welcome! If you have ideas or want to add support for other systems, please submit a pull request or open an issue.

See the [contribution guide](CONTRIBUTING.md) for more information.

## License

Fama is licensed under the [Open Smart City License](LICENSE.md) .

## Contact

For questions or support, please contact us at [fama@nedeco.de](mailto:fama@nedeco.de).
