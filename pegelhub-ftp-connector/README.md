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
* OpenJDK 17 installation ([e.g. OpenJDK17](https://www.oracle.com/java/technologies/downloads/)) (current working
  version: 17.0.11)
* Internet access (communication with server)
* Docker ([e.g. Docker Desktop](https://www.docker.com/products/docker-desktop/)) (current working version: 4.30.0)
    * be sure to enable all needed BIOS settings for Docker

## Setup

1) Build the pegelhub-ftp-connector project:
    * In your Terminal (CMD / Powershell) navigate to the project `../pegelhub-ftp-connector`
        * *OR* open the project with an IDE (IntelliJ IDEA as IDE recommended) inside of `../pegelhub-ftp-connector`
    * In your Terminal, execute command `mvn clean package -DskipTests` to build the jar project properly (Tests are
      currently failing, therefore skipping)
    * After a successful build a `pegelhub-ftp-connector-2023.12.jar` file will be added to
      the `pegelhub-ftp-connector/target` folder. The .jar and `properties.yaml` have to be in the same folder.
        * Move the new .jar file out of the `/target` folder into `pegelhub-ftp-connector`
        * ~~If you want to move the `properties.yaml` file, remember to use the new path of it in the JVM arguments when
          starting the FTP-Connector~~

The `properties.yaml` file stores information about the received data, including the provider and the needed API-Key for
communication between the Pegelhub-Core and the FTP-Connector.

2) Build the pegelhub-ftp-connector docker image:
    * Open the project in the correct folder like in Step #1
    * In Terminal, execute command `docker build .\Dockerfile`
    * _After successful build a_

## How to use

The FTP-Connector is capable of parsing .asc and .zrxp data. However, if you are planning on receiving both types of
data, you will need two separate instances of the FTP-Connector.

1. Make sure you have a `config.properties` File in the correct folder: `../pegelhub-ftp-connector/src/main/resources`
2. Define the corresponding properties to your needs:
    * For further explanation of the config properties refer to "Connector Options (Config Properties)" below.

```

```

As an example, a valid command for a zrxp-file-reading FTP Connector could be:

* The coreAddress is 127.0.0.1
* The Port is 8081
* The FTP-Server address is ftp.ServerName.org (could also be an IP-Address like the coreAddress)
* The FTP-Server port is 21
* The username is pegelReader
* The password is securePassword123
* There is no special path to the server data, therefor we use `/` (otherwise add the direct path)
* The expected data type is `zrxp`
* The time interval is 20 seconds

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
read.delay=20s
```

## Connector Options (Config Properties)

These arguments are passed inline (seperated by space) at the start of the FTP-Connector and have to be in the exact
order as listed below:

### core.address (formerly known as coreAddress)

* The address of the connector which is used to communicate with the Pegelhub-Core application

### core.port (formerly known as corePort)

* The port of the given coreAddress

### ftp.address (formerly known as pegelAddress / gaugeAddress)

* The address of the FTP-Server, which provides the data

### ftp.port (formerly known as pegelPort / gaugePort)

* The port of the FTP-Server, which provides the data

### ftp.user (formerly known as username)

* The user, which is used for authentication purposes

### ftp.password (formerly known as password)

* The password, which is used by the user to authenticate himself

### ftp.path (formerly known as path)

* The (absolute) path to the receivable data on the FTP-Server. If no special path is given, use `/`

### parser.type (formerly known as parserType)

* The parserType defines, which type of data can be parsed. Choose one of the two options: `asc` or `zrxp`

### read.delay (formerly known as readDelay)

* The readDelay defines the time interval in which the data is sent to the Core Application. The proper format
  is `number[s/m/h]` where `s`, `m` and `h` are *case-insensitive* and stand for seconds, minutes and hours.

## Possible errors / Troubleshooting

### Wrong JDK

Even if you have downloaded and installed the correct openJDK version it still could happen, that the project
remains on a different version.

In IntelliJ IDEA, make sure, that in the Project Settings (*accessible via Project
Structure == Ctrl + Alt + Shift + S*) you have selected the correct JDK. Versions especially lower or other than the
requested one (Prerequisites) could notably lead to an Error. Also make sure to set the Language Level to "SDK default".