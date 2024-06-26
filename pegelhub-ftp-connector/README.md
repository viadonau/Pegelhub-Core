# File-Transfer-Protocol Connector

This connector is used for fetching data from a given FTP-Server and parsing said data into "Entries". The following
file types are supported:

* .asc
* .zrxp

## Prerequisites

The FTP-Connector works independent of any operating system.
Prerequisites to install and use the FTP-Connector are:

* Apache Maven installation ([Latest Maven Release](https://maven.apache.org/download.cgi)) (current working version:
  Maven 3 - V 3.9.5)
* OpenJDK 17 or higher installation ([e.g. OpenJDK17](https://www.oracle.com/java/technologies/downloads/)) (current
  working version: 17.0.11)
* Internet access (communication with server)
* Docker ([e.g. Docker Desktop](https://docs.docker.com/get-docker/)) (current working version: 4.30.0)

You might need special (admin) permissions for executing the tasks below. For further details refer to [Possible errors /
Troubleshooting](#possible-errors--troubleshooting)

## Setup

### Build the pegelhub-ftp-connector project:

These steps are recommended to ensure that the project has the correct initial state.

* In your Terminal (CMD / Powershell) navigate to the project `../pegelhub-ftp-connector`
    * *OR* open the project with an IDE (IntelliJ IDEA as IDE recommended) inside of `../pegelhub-ftp-connector`
* In your Terminal, execute command `mvn clean package -DskipTests` to build the jar project properly (Tests are
  currently failing, therefore skipping)
* After a successful build a `pegelhub-ftp-connector-2023.12.jar` file will be added to
  the `pegelhub-ftp-connector/target` folder, besides the already existing `original-pegelhub-ftp-connector-2023.12.jar`

### Organise Files

* Make sure you have a `config.properties` and the `properties.yaml` File in the correct
  folder: `../pegelhub-ftp-connector/src/main/resources`
* Define the corresponding properties to your needs:
    * The `properties.yaml` file stores information about the received data, including the provider and the needed
      API-Key for communication between the Pegelhub-Core and the FTP-Connector
        * If you want the files in another directory, be sure to provide the correct path
    * In `properties.yaml` File change the following:
        * apiToken --> "generatedUniqueApiToken" (for generating a new Token, please refer to the Postman-Collection
          Documentation / Pegelhub-Library)
        * lastTokenRefresh --> make adjustments to the date, hour and minute mark depending on when you created the
          token
    * For further explanation of the config properties refer to [Connector Options (Config Properties)](#connector-options-config-properties) or [How to use](#how-to-use)
      below.

### Build and save the pegelhub-ftp-connector docker image:

* Open the project in the correct folder (`../pegelhub-ftp-connector`)
* In Terminal, execute command `docker build -t ftp-connector .` where the tag `ftp-connector` could also be renamed
  to another fitting name and  `.`  stands for the current path

To ensure that the image was built, execute `docker images` to show a list of built images

### If you want to run the container locally:

* Open the project in the correct folder (`../pegelhub-ftp-connector`)
* Be sure that the `properties.yaml` and `config.properties` Files are in the same folder and configured respectively.
  In this example they are both located under `./src/main/resources`
* Run the container with `docker run -v .\src\main\resources\:/app/files pegelhub-ftp-connector`

### If you want to run the container on a server (Linux):

* Make sure that your server meets the requirements of the Prerequisites
* On your local machine, open the project in the correct folder (`../pegelhub-ftp-connector`)
* Save the docker image with `docker save ftp-connector -o ftp-connector.tar`
* Transfer the data onto your provided server with `scp .\ftp.connector.tar serverUsername@IPAddressOfServer:~`
    * You may need to enter credentials (password for the given username)
* On your server, load the docker image with `docker load -i ftp-connector.tar`
* On your server, run the container with `docker run -d -v ./ftp-connector.tar:/app/files ftp-connector`
    * -d allows you to run the container as a background-task so that you can still use or close the terminal without
      interrupting the container

To ensure that the container is running, execute `docker ps` to show a list of available docker containers.
To stop a running container, execute `docker stop yourContainerID`.
To remove a container, execute `docker rm yourContainerID`

For further operations with docker refer to the [official documentation](https://docs.docker.com/reference/).

## How to use

Read the readme in the postman collection folder on how to generate an api token.
`apiToken`s cannot be reused for applications using the API-Client. It must be unique for each connector instance.

1) Build the Maven Project pegelhub-ftp-connector
2) Open `config.properties`:
    * Set your desired configurations
3) Open `properties.yaml`:
    * Set a valid API Token and update the timestamp to the time of creation (change Date, Hour, Minute mark)
    * Set your desired configurations
4) Organise all Files into one folder (`./src/main/resources`)
5) Build the Docker Images and Containers
6) Run the Containers on your desired Machine (Local/Server)

The FTP-Connector is capable of parsing .asc and .zrxp data. However, if you are planning on receiving both types of
data, you will need two separate instances of the FTP-Connector.

### Example for config.properties

As an example, valid properties for a zrxp-file-reading FTP Connector could be:

* The coreAddress is 127.0.0.1
* The Port is 8081
* The FTP-Server address is ftp.ServerName.org (could also be an IP-Address like the coreAddress)
* The FTP-Server port is 21
* The username is pegelReader
* The password is securePassword123
* There is no special path to the server data, therefor we use `/` (otherwise add the direct path)
* The expected data type is `zrxp`
* The time interval is 15 minutes

The `config.properties` File should therefore look like this:

```
core.address=127.0.0.1
core.port=8081
ftp.address=ftp.viadonau.org 
ftp.port=21
ftp.user=pegelReader
ftp.password=securePassword123
ftp.path=/
parser.type=zrxp
read.delay=15m
```

## Connector Options (Config Properties)

These arguments are passed at the start of the FTP-Connector and have to be in the exact order as listed below:

### core.address

* The address of the connector which is used to communicate with the Pegelhub-Core application

### core.port

* The port of the given coreAddress

### ftp.address

* The address of the FTP-Server, which provides the data

### ftp.port

* The port of the FTP-Server, which provides the data

### ftp.user

* The user, which is used for authentication purposes

### ftp.password

* The password, which is used by the user to authenticate himself

### ftp.path

* The (absolute) path to the receivable data on the FTP-Server. If no special path is given, use `/`

### parser.type

* The parserType defines, which type of data can be parsed. Choose one of the two options: `asc` or `zrxp`

### read.delay

* The readDelay defines the time interval in which the data is sent to the Core Application. The proper format
  is `number[s/m/h]` where `s`, `m` and `h` are *case-insensitive* and stand for seconds, minutes and hours.

## Possible errors / Troubleshooting

### Wrong JDK

In case you do the tasks with an IDE such as IntelliJ IDEA: Even if you have downloaded and installed the correct
openJDK version it still could happen, that the project remains on a different version.

In IntelliJ IDEA, make sure, that in the Project Settings (*accessible via Project Structure == Ctrl + Alt + Shift + S*)
you have selected the correct JDK. Versions especially lower than the requested one (Prerequisites) could lead
to an Error. Also make sure to set the Language Level to "SDK default".

### Wrong permissions/privileges

For executing the tasks you might need admin permissions. Depending on how you access the project, you might have to run
the programs (Terminal, IntelliJ, Docker Desktop) as admin. 