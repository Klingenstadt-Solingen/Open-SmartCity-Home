# Contributing

We value contributions from the community! To maintain project quality and ensure smooth collaboration, please follow the guidelines below when contributing.
If you have ideas or want to add support for other systems, please submit a pull request or open an issue.

## Prerequisites
- **Home System:** Ensure you have access to a running home system like Home Assistant, OpenHAB, or ioBroker. These systems are required to test Open SmartCity Home's integration and functionality.

## Installation
1. Fork this repository on GitHub. For detailed instructions, refer to the [GitHub guide on forking a repository](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/working-with-forks/fork-a-repo).

2. Configure the application:
    - Copy and rename the `.env.example` file to `.env`, then update the file with the desired settings. These are critical for connecting Open SmartCity Home to your home system.
    - Update the RabbitMQ connection settings in the configuration file.
    - Update the MQTT or ioBroker API settings as needed.
    - Ensure that all required dependencies like [Java 21](https://www.oracle.com/de/java/technologies/downloads/#java21) are installed and your environment meets the prerequisites before building and running the application.

3. Build and run the application:
```bash
./gradle_env.sh run
```


## Configuration
Open SmartCity Home uses a configuration file to manage its settings. The following parameters must be set:

**RabbitMQ Settings.**

- **RABBIT_MQ_STOMP_URL:** RabbitMQ STOMP server address.
    
    For development purposes you can use this:
    ```
    wss://digitaltwin.service.nedeco.digital:15673/ws
    ```

- **RABBIT_MQ_STOMP_USERNAME:** RabbitMQ username.
    
    For development purposes you can use this:
    ```
    smart-home
    ```

- **RABBIT_MQ_STOMP_PASSWORD:** Password for the user.
    
    For development purposes you can use this:
    ```
    7fmpmh4kw4UGuEmanyfBBHEGvNc6a4KXi7bE9Xt4CiEJmpTjwAQT
    ```

- **SMART_HOME_TYPE:** Type of home system.
    - **HA** for Home Assistant and OpenHAB
    - **IB** for ioBroker
- **Home Assistant/OpenHAB specific:**
  - **MQTT_USERNAME:** Username to connect to Open SmartCity Home's MQTT.
  - **MQTT_PASSWORD:** Password
- **ioBroker specific:**
  - **IO_BROKER_URL:** ioBroker's REST API url.


## Connecting with home system

See the following documentation on how to set up a home system of choice with Open SmartCity Home.

Start the appropriate Docker container for your home system using the commands below. Ensure [Docker](https://docs.docker.com/get-started/introduction/) is installed and running on your system.

Start Docker with your selected home system using one of the predefined configurations in the [docker-compose.yml](/docker-compose.yml) file.

- **Home Assistant:** 
    ```
    docker compose run homeassistant
    ```
    See the documentation on how to [connect Home Assistant to Open SmartCity Home](/documentation/homesystem/HOMEASSISTANT.md)

- **OpenHAB:** 
    ```
    docker compose run openhab
    ```
    See the documentation on how to [connect OpenHAB to Open SmartCity Home](/documentation/homesystem/OPENHAB.md)

- **ioBroker:** 
    ```
    docker compose run iobroker
    ```
    See the documentation on how to [connect ioBroker to Open SmartCity Home](/documentation/homesystem/IOBROKER.md)

## Commit Messages
We use [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) to structure our commit messages.

## Branch Names
Branch names should follow the "TYPE/NAME" structure and be written in "kebab case".

Examples of Types:
- chore: Maintenance tasks.
- docs: Documentation updates.
- feat: New features.
- fix: Bug fixes.
- refactor: Code restructuring without adding new features.
- test: Adding or updating test cases.

Example Branch Names:
- feat/new-function
- chore/update-dependency
- fix/program-crash

## Changelog
All changes made need to listed in the Changelog. See [Changelog](/CHANGELOG.md) for more Information.

## Pull Request Rules  

When submitting a pull request, please adhere to the following rules:  

1. **Descriptive Titles and Details**:  
   - Use clear and descriptive titles for your pull requests.  
   - Include a summary of what the pull request does, why it's needed, and any important details.  

2. **Link Issues**:  
   - If the pull request addresses or resolves an issue, link it using keywords like `Fixes #123` or `Closes #456`.  

3. **Small, Focused Changes**:  
   - Keep pull requests small and focused on a single change or feature to make reviews easier.  

4. **Tests**:  
   - Including tests is highly encouraged, especially for features or bug fixes that impact critical parts of the application.

5. **Code Review**:  
   Contributors who take ownership of features or modules are expected to:
    - Respond to issues and questions related to their contributions.
    - Update their code to align with future project requirements." 

6. **Follow the Coding Style**:  
   - Use the same coding style and conventions as the existing codebase.  

## Code Ownership  

To maintain accountability and ensure high-quality contributions:  

- **Feature/Module Ownership**:  
   - Contributors who submit significant changes or new features may be asked to take ownership of that part of the codebase. This means:  
     - Helping maintain it over time.  
     - Addressing issues and questions related to it.  
     - Updating or improving it as needed.  

- **Collaborative Ownership**:  
   - Code ownership is shared among the community. While specific contributors may lead certain areas, everyone is encouraged to review and contribute to all parts of the project.  

## Summary  

By following these guidelines, you help us maintain a clean, stable, and collaborative codebase. We appreciate your contributions and look forward to working together to improve this project!

If you have any questions or need clarification about these guidelines, don't hesitate to reach out to the maintainers. We’re here to help!
