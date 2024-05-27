# Time-Series-Transfer-Protocol Connector

This connector is used for reading or writing data using the Time-Series-Transfer-Protocol (TSTP).

## Prerequisites

The TSTP-Connector runs as a container, making it independent of any operating system. 
To install and use the TSTP-Connector, you need:

- Docker installed
- Access to a Pegelhub-Core and TSTP-Server

## Setup and Usage

1. Build the pegelhub-tstp-connector project:
    - Open the project inside `../pegelhub-tstp-connector` or navigate to the project via CMD/Powershell under the same path.
    - In Terminal, execute the command:
      ```sh
      mvn clean package
      ```

2. Configure the `properties.yaml` file:
    - This file contains information about the received data, including the provider and the required API-Key for communication between the Pegelhub-Core and the TSTP-Connector.
    - The Connector behavior changes based on the `isSupplier` setting:
        - `true`: The Connector reads from the TSTP-Server and sends data to the Pegelhub-Core.
        - `false`: The Connector sends data to the TSTP-Server from the Pegelhub-Core.

3. Adjust the configurations in the `config.properties` file according to your server/core settings.

4. Fill in the `properties.yaml` so that your connector registers correctly with the Pegelhub.

5. Move or copy the `config.properties` and `properties.yaml` into a new folder.

6. Build the Docker container:
   ```sh
   docker build -t pegelhub-tstp-connector:latest .
   ```

7. Start the Docker container:
   ```sh
   sudo docker run -d -v ./NAME_OF_NEW_FOLDER_WITH_CONFIGS:/app/files pegelhub-tstp-connector
   ```

## Connector Options

These arguments are passed via the `config.properties` file at the start of the TSTP-Connector.

### core.address
- The address used to communicate with the Pegelhub-Core application.

### core.port
- The port of the given core address.

### tstp.address
- The address of the TSTP-Server.

### tstp.port
- The port of the TSTP-Server.

### connector.readDelay
- The `readDelay` defines the time interval for reading/writing data. The correct format is `number[s/m/h]`, 
where `s`, `m`, and `h` are *case-insensitive* and represent seconds, minutes, and hours, respectively.

## Usage without Docker

If you want to start the connector locally for debugging or similar, you just need to adjust the file paths
in the main class.