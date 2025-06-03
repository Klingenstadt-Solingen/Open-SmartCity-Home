# ioBroker

This guide explains how to integrate the Open SmartCity Home application with ioBroker using the REST API adapter for weather sensor data processing.

## Prerequisites

Ensure you have ioBroker installed and running on your system before starting this guide.

Make sure ioBroker is updated to the latest version to ensure compatibility with the Open Smart City Home application.

You can find the official ioBroker documentation at the following link:
[ioBroker Documentation](https://www.iobroker.net/#en/documentation/README.md)

This site contains detailed guides, tutorials, and information about adapters, installation, and configuration.

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
  -e SMART_HOME_TYPE=IB \
  -e IO_BROKER_URL=http://<iobroker-ip>:8081 \
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
      SMART_HOME_TYPE: "IB"
      IO_BROKER_URL: "http://<iobroker-ip>:8081"
```

To start Open Smart City Home using Docker Compose, run:
```sh
docker-compose up -d
```

## Environment Variables
Open Smart City Home requires several environment variables to function correctly. Below is a list of the key variables:

| Variable | Description |
|----------|-------------|
| `SMART_HOME_TYPE` | Type of home automation system (`IB` for ioBroker) |
| `IO_BROKER_URL` | URL of ioBroker (required for ioBroker integration) |

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

## Adapters

After installation, access the ioBroker web interface at:

```
http://<iobroker-ip>:8081/
```

If you run ioBroker locally, you can open it via [http://localhost:8081]().

The REST API adapter allows ioBroker to receive data from external applications, such as Open Smart City Home, via API requests.

1. Navigate to the **"Adapters"** tab.
2. Search for the **"REST API"** adapter.

<p>
  <img width="600px" alt="logo" src="../images/iobroker_adapters.png">
</p>

3. Install the adapter:
    - Click on the triple dots button.
    - Select the **"Add instance"** button (bottom left).

    <p>
    <img width="600px" alt="logo" src="../images/iobroker_adapters2.png">
    </p>

    - Wait for the installation process to complete.

    <p>
    <img width="600px" alt="logo" src="../images/iobroker_adapters3.png">
    </p>


Once installed, you will see the **"rest-api"** entry under the **"Instances"** tab.

## Conclusion

Verify that the Open Smart City Home application is configured to send data to the ioBroker REST API endpoint. The API URL for ioBroker is required to send messages. By default, it will look like this:
```
http://<iobroker-ip>:8093/v1/
```

To improve usability, configure the **"Objects"** view in ioBroker to display custom names. Click the 'Configure' button and follow the steps below.

<p>
  <img width="600px" alt="logo" src="../images/iobroker_objects.png">
</p>

Disable the **"Auto"** option to allow custom columns and enable the **"Name"** column, making it easier to identify objects.

<p>
  <img width="600px" alt="logo" src="../images/iobroker_objects2.png">
</p>


Once the Open Smart City Home application is running and messages are being sent, you can view them in the **"Objects"** tab of the ioBroker web interface.

<p>
  <img width="600px" alt="logo" src="../images/iobroker_objects3.png">
</p>

## Troubleshooting

If you do not see any data in the Objects tab:

- Confirm that the Open Smart City Home application is running and sending data to the correct API endpoint.
- Check the ioBroker logs for any errors related to the REST API adapter.
- Ensure that the REST API adapter instance is active and configured properly.
