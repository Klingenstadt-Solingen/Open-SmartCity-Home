# Home Assistant

This guide explains how to integrate the Open SmartCity Home application with Home Assistant using MQTT for seamless weather sensor data integration.

## Prerequisites

Ensure you have a running instance of Home Assistant to integrate weather sensor data from the Open SmartCity Home application.

Ensure Home Assistant is updated to the latest version for compatibility with MQTT integration.

You can find the official Home Assistant documentation at the following link:
[Home Assistant Documentation](https://www.home-assistant.io/docs/)

This site contains detailed guides, tutorials, and information about installation, configuration, and integrations.

## Setting Up Open SmartCity Home as a Home Assistant Addon

Click on the button below:

[![Open your Home Assistant instance and show the add add-on repository dialog with a specific repository URL pre-filled.](https://my.home-assistant.io/badges/supervisor_add_addon_repository.svg)](https://my.home-assistant.io/redirect/supervisor_add_addon_repository/?repository_url=https://github.com/nedeco/fama-hass-addons)

## Setting Up Open Smart City Home with Docker

Open Smart City Home can be deployed using Docker, either with a simple `docker run` command or using `docker-compose` for more complex setups.

### Prerequisites
Before running Open Smart City Home, ensure you have the following installed:
- Docker ([Installation Guide](https://docs.docker.com/get-docker/))
- Docker Compose (if using `docker-compose.yml` setup)
- A home automation system (Home Assistant, OpenHAB, or ioBroker)

## Running Open Smart City Home with `docker run`
You can start Open Smart City Home with a single `docker run` command, passing the required environment variables:

```sh
docker run -d --name fama \
  -e SMART_HOME_TYPE=HA \
  -e MQTT_USERNAME=my_mqtt_user \
  -e MQTT_PASSWORD=my_mqtt_password \
  devopsnedeco/fama
```

## Running Open Smart City Home with Docker Compose
For more flexibility, you can use a `docker-compose.yml` file:

```yaml
services:
  fama:
    image: devopsnedeco/fama
    container_name: fama
    restart: unless-stopped
    ports:
      - 1883:1883
    environment:
      SMART_HOME_TYPE: "HA"
      MQTT_USERNAME: "my_mqtt_user"
      MQTT_PASSWORD: "my_mqtt_password"
```

To start Open Smart City Home using Docker Compose, run:
```sh
docker-compose up -d
```

## Environment Variables
Open Smart City Home requires several environment variables to function correctly. Below is a list of the key variables:

| Variable | Description |
|----------|-------------|
| `SMART_HOME_TYPE` | Type of home automation system (`HA` for Home Assistant/OpenHAB) |
| `MQTT_URL` | Optinal External MQTT broker URL (required for Home Assistant/OpenHAB integration) |
| `MQTT_USERNAME` | MQTT broker username (required for Home Assistant/OpenHAB integration) |
| `MQTT_PASSWORD` | MQTT broker password (required for Home Assistant/OpenHAB integration) |

## Verifying the Setup
Once Open Smart City Home is running, you can verify its status using:
```sh
docker ps
```
This should display a running container named `fama`.

To check logs for troubleshooting:
```sh
docker logs -f fama
```

## MQTT Configuration

### Access Home Assistant

After completing the installation of Home Assistant, access its web interface at:
```
http://<home-assistant-ip>:8123/
```
If you are running Home Assistant locally, you can open it via [http://localhost:8123](http://localhost:8123).

### Set Up MQTT Integration in Home Assistant

1. Navigate to **Settings > Devices & Services > Add Integration**.

<p>
  <img width="600px" alt="logo" src="../images/homeassistant_settings.png">
</p>

2. Search for **MQTT** and select it.

<p>
  <img width="600px" alt="logo" src="../images/homeassistant_mqtt_integration.png">
</p>
<p>
  <img width="600px" alt="logo" src="../images/homeassistant_mqtt_settings.png">
</p>

3. Configure the MQTT broker:
   - **Broker Hostname/IP:** Enter the hostname or IP address of the Open Smart City Home application's MQTT broker.
   - **Port:** Set the port to `1883`.
   - **Username:** Enter the username for the MQTT broker.
   - **Password:** Enter the corresponding password.

   Ensure the username and password match the credentials configured in the Open Smart City Home application's MQTT settings.

<p>
  <img width="600px" alt="logo" src="../images/homeassistant_mqtt_settings2.png">
</p>

4. Click **Submit** to save the settings.

  Note: Ensure that MQTT discovery is enabled in Home Assistant. This allows Open Smart City Home's sensor data to be automatically detected and displayed.

## Conclusion

Ensure that the Open Smart City Home application is configured correctly to send MQTT messages. The MQTT username and password are required to connect to Open Smart City Home via MQTT. Once the Open Smart City Home application receives messages via the RabbitMQ, it send message via MQTT. You can verify the integration by navigating to the Devices or Entities section in Home Assistant. Weather sensor data should appear as newly discovered devices or entities.

<p>
  <img width="600px" alt="logo" src="../images/homeassistant_stations.png">
</p>

## Troubleshooting

If you do not see sensors in your Home Assistant dashboard:

  - Verify the MQTT broker connection settings.
  - Ensure the Open Smart City Home application is configured correctly to send MQTT messages.
  - Check the Home Assistant logs for any errors related to MQTT integration.
