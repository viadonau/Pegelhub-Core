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
* OpenJDK 18 installation ([e.g. OpenJDK18](https://openjdk.org/projects/jdk/18/)) (current working version: 18.0.2)
* Internet access (communication with server)

## Setup

1) Build the pegelhub-ftp-connector project:
    * Open the project inside of `../pegelhub-ftp-connector`
        * *OR* access project via CMD ( / Powershell) under the same path
    * In Terminal, execute command `mvn clean package -DskipTests` (Tests are currently failing, therefore skipping)
    * After a successful build a `pegelhub-ftp-connector-2023.12.jar` file will be added to
      the `pegelhub-ftp-connector/target` folder. The .jar and `properties.yaml` have to be in the same folder.
        * Move the new .jar file out of the `/target` folder into `pegelhub-ftp-connector`
        * If you want to move the `properties.yaml` file, remember to use the new path of it in the JVM arguments when
          starting the FTP-Connector

The `properties.yaml` file stores information about the received data, including the provider and the needed API-Key for
communication between the Pegelhub-Core and the FTP-Connector.

## How to use

The FTP-Connector is capable of parsing .asc and .zrxp data. However, if you are planning on receiving both types of
data, you will need two separate instances of the FTP-Connector.

1. Make sure you are in the correct root folder: `../pegelhub-ftp-connector`
2. Access the terminal (if needed, with admin permission) and execute the command with the corresponding arguments:
    * For further explanation of the JVM arguments refer to "Connector Options (JVM arguments)" below.

```
java -jar pegelhub-ftp-connector-2023.12.jar <coreAddress> <corePort> <pegelAddress> <pegelPort> <username> <password> <path> <parserType> <readDelay> <propertiesFile>
```

As an example, a valid command could be:

* The coreAddress is 127.0.0.1
* The Port is 8081
* The FTP-Server address is ftp.ServerName.org (could also be an IP-Address like the coreAddress)
* The FTP-Server port is 21
* The username is pegelReader
* The password is securePassword123
* There is no special path to the server data, therefor we use `/`
* The expected data type is `zrxp`
* The time interval is 20 seconds
* There is no special path to the `properties.yaml` file, therefor we only use the filename

```
java -jar pegelhub-ftp-connector-2023.12.jar 127.0.0.1 8081 ftp.ServerName.org 21 pegelReader securePassword123 / zrxp 20s properties.yaml
```

## Connector Options (JVM arguments)

These arguments are passed inline (seperated by space) at the start of the FTP-Connector and have to be in the exact
order as listed below:

### coreAddress

* The address of the connector which is used to communicate with the Pegelhub-Core application

### corePort

* The port of the given coreAddress

### pegelAddress / gaugeAddress

* The address of the FTP-Server, which provides the data

### pegelPort / gaugePort

* The port of the FTP-Server, which provides the data

### username

* The user, which is used for authentication purposes

### password

* The password, which is used by the user to authenticate himself

### path

* The (absolute) path to the receivable data on the FTP-Server. If no special path is given, use `/`

### parserType

* The parserType defines, which type of data can be parsed. Choose one of the two options: `asc` or `zrxp`

### readDelay

* The readDelay defines the time interval in which the data is sent to the Core Application. The proper format
  is `number[s/m/h]` where `s`, `m` and `h` are *case-insensitive* and stand for seconds, minutes and hours.

### propertiesFile

* The `properties.yaml` location (path to file) which describes the data supplier. Keep in mind, if the connector has to
  run on a Windows System it is mandatory to escape the "escape character" function of back-slashes. This means every
  backslash ` \ ` should look like ` \\ ` (e.g. if you have a specific path to provide and the file is not in the root folder).

## Possible errors / Troubleshooting

### NoSuchFieldError

This could be caused due to a wrong openJDK.

Even if you have downloaded and installed the correct openJDK 18 version it could still happen, that the project
remains on a different version.

In IntelliJ IDEA, make sure, that in the Project Settings (*accessible via Project
Structure == Ctrl + Alt + Shift + S*) you have selected the correct JDK. Versions lower or higher than 18 will notably
lead to a NoSuchFieldError.