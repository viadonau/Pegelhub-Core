# Inter-Cluster-Communication Connector

This connector is used for syncing data of selected suppliers between two PegelHub clusters.

## Prerequisites

The ICC-Connector works independent of any operating system.
Prerequisites to install and use the ICC-Connector are:

* Apache Maven installation ([Latest Maven Release](https://maven.apache.org/download.cgi)) (current working version:
  Maven 3 - V 3.9.5)
* OpenJDK 17 or higher installation ([e.g. OpenJDK17](https://www.oracle.com/java/technologies/downloads/)) (current
  working version: 17.0.11)
* Internet access (communication with server)
* Docker ([e.g. Docker Desktop](https://docs.docker.com/get-docker/)) (current working version: 4.30.0)

You might need special (admin) permissions for executing the tasks below. For further details refer to [Possible errors /
Troubleshooting](#possible-errors--troubleshooting)

## Setup

### Build the pegelhub-library Project:

These steps are recommended to ensure that the project has the correct initial state.

* In your Terminal (CMD / Powershell) navigate to the project `../pegelhub-library`
    * *OR* open the project with an IDE (IntelliJ IDEA as IDE recommended) inside of `../pegelhub-library`
* In your Terminal, execute command `mvn clean package -DskipTests` to build the jar project properly (Tests are
  currently failing, therefore skipping)
* After a successful build a `pegelhub-library-1.0.0.jar` and `pegelhub-library-1.0.0-sources.jar` file will be
  added to the `pegelhub-library/target` folder.

### Build the pegelhub-icc-connector Project:

These steps are recommended to ensure that the project has the correct initial state.

* In your Terminal (CMD / Powershell) navigate to the project `../pegelhub-icc-connector`
    * *OR* open the project with an IDE (IntelliJ IDEA as IDE recommended) inside of `../pegelhub-icc-connector`
* In your Terminal, execute command `mvn clean package -DskipTests` to build the jar project properly (Tests are
  currently failing, therefore skipping)
* After a successful build a `pegelhub-icc-connector-2022.01.jar` file will be added to
  the `pegelhub-icc-connector/target` folder, besides the already existing `original-pegelhub-icc-connector-2022.01.jar`

### Organise Files

* Make sure you have a `config.properties` and the `sink_properties.yaml` as well as the `source_properties.yaml` File
  in the correct folder: `../pegelhub-icc-connector/src/main/resources`
* Define the corresponding properties to your needs:
    * The `sink_properties.yaml` and `source_properties.yaml` files store information about receiving and sending
      clusters, also including the needed API-Key for authentication purposes.
        * If you want the files in another directory, be sure to provide the correct path
    * In each `properties.yaml` File change the following:
        * apiToken --> "generatedUniqueApiToken" (for generating a new Token, please refer to the Postman-Collection
          Documentation / Pegelhub-Library)
        * lastTokenRefresh --> make adjustments to the date, hour and minute mark depending on when you created the
          token
    * For further explanation of the config properties refer to [Connector Options (Config Properties)](#connector-options-config-properties) below.

### Build and save the pegelhub-icc-connector docker image:

* Open the project in the correct folder (`../pegelhub-icc-connector`)
* In Terminal, execute command `docker build -t icc-connector .` where the tag `icc-connector` could also be renamed
  to another fitting name and  `.`  stands for the current path

To ensure that the image was built, execute `docker images` to show a list of built images

### If you want to run the container locally:

* Open the project in the correct folder (`../pegelhub-icc-connector`)
* In Terminal, load the docker image with `docker load -i icc-connector`
* Be sure that the `sink_properties.yaml`, `source_properties.yaml` and `config.properties` Files are in the same
  folder and configured respectively. In this example they are all located under `./src/main/resources`
* Run the container with `docker run -v .\src\main\resources\:/app/files pegelhub-icc-connector`

### If you want to run the container on a server (Ubuntu):

* Make sure that your server meets the requirements of the Prerequisites
* On your local machine, open the project in the correct folder (`../pegelhub-icc-connector`)
* Save the docker image with `docker save icc-connector -o icc-connector.tar`
* Transfer the data onto your provided server with `scp .\icc.connector.tar serverUsername@IPAddressOfServer:~`
    * You may need to enter credentials (password for the given username)
* On your server, load the docker image with `docker load -i icc-connector.tar`
* On your server, run the container with `docker run -d -v ./icc-connector.tar:/app/files icc-connector`
    * -d allows you to run the container as a background-task so that you can still use or close the terminal without
      interrupting the container

To ensure that the container is running, execute `docker ps` to show a list of available docker containers.
To stop a running container, execute `docker stop yourContainerID`.
To remove a container, execute `docker rm yourContainerID`

For further operations with docker refer to the [official documentation](https://docs.docker.com/reference/).

## How to use

Read the readme in the postman collection folder on how to generate an api token.
`apiToken`s cannot be reused for applications using the API-Client. It must be unique for each connector instance.

1) Build the Maven Project, both pegelhub-library and pegelhub-icc-connector 
2) Start two PegelHub clusters:
    * The two clusters must have their unique HTTP(S) addresses
    * They don't need to run on separate machine
3) Acquire the HTTP(S) addresses of the two PegelHub instances
4) Open `config.properties`:
    * Set `Core.Source` as the HTTP(S) address of the PegelHub where data is fetched from (e.g. `http://localhost:8080/`)
    * Set `Core.Sink` as the HTTP(S) address of the PegelHub where data is sent to (e.g. `http://localhost:8081/`)
    * Set the desired interval for `Icc.RefreshInterval` (e.g. `40m`)
    * Set `Icc.SourceStationNumber` as the station numbers of the supplier station numbers whose data should be synced
      with the `Core.Sink`, separated using `,` (e.g. `ST1,ST3,Station6`)
5) Open `sink_properties.yaml`:
    * Generate and activate new token in the PegelHub instance that is set as the `Core.Sink`
    * Set this token as `apiToken`
    * Make sure `isSupplier` is set to `true`
    * If there is a node `taker`, remove it
    * Configure everything below `supplier` to your liking
        * `supplier.connector.number` and `supplier.id` must be unique within a PegelHub cluster
6) Open `source_properties.yaml`:
    * Generate and activate new token in the PegelHub instance that is set as the `Core.Source`
    * Set this token as `apiToken`
    * Make sure `isSupplier` is set to `false`
    * If there is a node `supplier`, remove it
    * Configure everything below `taker` to your liking
        * `taker.connector.number` and `taker.id` must be unique within a PegelHub cluster
7) Organise all Files into one folder (`./src/main/resources`)
8) Build the Docker Images and Containers
9) Run the Containers on your desired Machine (Local/Server)

## Connector Options (Config Properties)

These arguments are passed at the start of the ICC-Connector and have to be in the exact order as listed below:

### Core.Source

* The HTTP(S) address of the PegelHub where data is fetched from

### Core.Sink

* The HTTP(S) address of the PegelHub where data is sent to

### Icc.RefreshInterval

* The interval in which the ICC-Connector syncs the data from two clusters and where `Core.sink` can receive data again
* Supported formats are:
    * `24H` or `24h` => every 24 hours
    * `20M` or `20m` => every 20 minutes
    * `45S` or `45s` => every 45 seconds
    * These cannot be combined (e.g. `24H20M` is not valid!)

### Icc.SourceStationNumber

* The supplier station numbers whose data should be synced with the `Core.Sink`, separated using `,`(
  e.g. `ST1,ST3,Station6`)

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