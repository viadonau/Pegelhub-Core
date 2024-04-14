# Pegelhub Core

----------------------------------------------------------------------

## Architecture

![](./../docs/readme-imgs/ph_core_architecture.png)

External systems communicate with the core via __REST__ interfaces, one for level data and one for metadata.

The __logic layer__ similarly offers separate services for level data and metadata.

Communication with the databases (data store for level data, meta store for metadata) is handled by the __data persistor service__.

<br> 
<br>

## Modules

![](./../docs/readme-imgs/modules_relations.png)


### inbound-http
Responsible for processing REST requests from the user and handling authentication. 

### logic
Serves as the communication layer between the external and internal layers (outbound and inbound).


### logic-api
Contains the contract between the logic and inbound layer: defines which criteria the logic needs to fulfill and what 
the inbound layer can use. The module primarily contains interfaces.

### outbound-api
Contains the contract between the outbound and inbound layer: defines which criteria the logic needs to fulfill and what
the outbound layer can use. The module primarily contains interfaces.

### outbound-influx
Handles communication with the Influx database.

### outbound-jdbc
Handles communication with the Postgres database.

### routing-proxy
Responsible for distributing requests to all instances of the core.

### service-registry
Registers all instances of the core and the routing proxy.

### shared
Contains data contents which needs to be shared between the outbound-api and logic-api.


### shared-test-utils
Provides the configuration for various test settings, such as integration tests.

### starter
Holds the configuration for the Spring project and the Startup

--------------------------------------------------

<br> 
<br>

## Needed Software for local development

### Windows
Download [Docker Desktop](https://www.docker.com/products/docker-desktop/).

Download and install a Java JDK of version 17 or higher ([e.g. OpenJDK17](https://openjdk.org/projects/jdk/17/)).

Download and install the latest version of [Apache Maven](https://maven.apache.org/download.cgi).

### Linux

Download [Docker](https://docs.docker.com/engine/install/ubuntu/).

- sudo apt-get install docker
- sudo apt-get install docker-compose

Download and install a Java JDK of version 17 or higher ([e.g. OpenJDK17](https://openjdk.org/projects/jdk/17/)).

- sudo apt-get install openjdk-17-jdk

Download and install the latest version of [Apache Maven](https://maven.apache.org/install.html).

- sudo apt-get install maven

### Required for Development

Download and install the latest versions of [IntelliJ IDEA](https://www.jetbrains.com/idea/download/).

- The ultimate edition of IntelliJ is recommended, otherwise the run configurations might not work 

<br> 
<br>

----------------------------

## Starting the Core using IntelliJ

Before running any configuration in IntelliJ, please rebuild the project and run the pegelhub-core maven configuration.

### For Linux
Open a terminal in the pegelhub-core root folder and run the following commands

- sudo chmod a+x ./init-influxdb.sh 

### Dev version using IntelliJ

1. Make sure that Docker is running.

2. Open a terminal in the pegelhub-core root folder and run the following commands

```
docker run --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=pegelhub -d postgres

docker run --name influxdb -p 8086:8086 -e DOCKER_INFLUXDB_INIT_MODE=setup -e DOCKER_INFLUXDB_INIT_USERNAME=admin -e DOCKER_INFLUXDB_INIT_PASSWORD=admin1234 -e DOCKER_INFLUXDB_INIT_ORG=pegelhub -e DOCKER_INFLUXDB_INIT_BUCKET=pegelhub -e BUCKET_TELEMETRY=telemetry -e BUCKET_DATA=data -e STORE_USER=pegelhub -v $pwd/init-influxdb.sh:/docker-entrypoint-initdb.d/00-setup.sh -v $pwd/.datastoreconfig:/etc/influxdb2/store/ -d influxdb:latest
```

3. Copy tokens from `.datastoreconfig/storeapp.yaml` into `starter/src/main/resources/application-dev.yaml`
   ![](./../docs/readme-imgs/token_copy.png)

4. Run the LocalDevelopment configuration.
   ![](./../docs/readme-imgs/local_config.png)

### Compose version using IntelliJ

1. Make sure that Docker is running.

2. Open the folder `pegelhub/pegelhub-core` as IntelliJ project.

   Run the following configuration

   ![](./../docs/readme-imgs/configurations.png)  
   ![](./../docs/readme-imgs/mvn.png)

   OR

   open a terminal and execute
    ```
    mvn clean package
    ```

   Wait until `mvn clean package` is complete.

3. Edit the following configuration:

   ![](./../docs/readme-imgs/docker.png)

   Set `environment variables file` to your local path to `<PATH_TO_REPO>/pegelhub-core/testenvironment.vars`.

    Press `OK` to save the configuration.


4. Run the configuration (`docker-compose.yaml`) in order to start the pegelhub-core.

- Alternatively run following command:
  - docker-compose --env-file ./testenvironment.vars up --build -d

### Compose version without IDE

1. Open a terminal in the pegelhub-core root folder and run the following commands

```
mvn clean package

docker-compose --env-file ./testenvironment.vars up --build -d
```

### Known errors
If the influx database container keeps shutting down, check if the `.int-influxdb.sh` 
file uses `LF` as a line separator.
![](./../docs/readme-imgs/db_change.png)

<br> 
<br>
-----------------------------------------------------------------

## Rolling updates

To perform deployment updates without downtime, use docker-compose.

1. Run maven clean package.

2. Start 6 replicas of the starter service.
   The --no-recreate flag prevents Compose from picking up changes.
```
docker-compose --env-file ./testenvironment.vars up -d --scale starter=6 --no-recreate --build
```

3. Reset number of services.
```
docker-compose up -d --scale starter=3 --no-recreate
```


<br> 
<br>
-----------------------------------------------------------------

# Server Environment Setup

## Preparation on Local Development Machine
Follow these steps on your local machine to prepare the application and its environment for deployment:

### 1. Build the Application
Execute the following command to build the application:
   ```bash
   mvn clean package
   ```

### 2. Build Docker Images
Build Docker images for `registry`, `starter` and `proxy`. For instance, to build the `registry`:
   ```bash
   cd ./service-registry
   docker build . --tag registry:latest
   docker save registry:latest > registry.tar
   ```
Repeat the steps above for `starter` and `proxy`.

### 3. Organize Deployment Files
Gather the following files into one directory:
    - The Docker `.tar` images (`registry.tar`, `starter.tar`, `proxy.tar`)
    - `docker-compose.yml`
    - `init-influxdb.sh`
    - `testenvironment.vars`
    - `.datastoreconfig`

### 4. Compress the Directory
Use this command to compress the directory:
   ```bash
   tar -czvf pegelhub.tar.gz directory_to_compress
   ```

### 5. Transfer to Server
Send the compressed file to the server using SCP:
   ```bash
   scp pegelhub.tar.gz YOUR_USER@IP_OF_SERVER:~
   ```

## Setup on the Server
Execute these steps on your server to deploy the application:

### 1. Install Docker and Docker Compose
Install Docker and Docker Compose using:
   ```bash
   sudo apt-get update
   sudo apt-get install docker.io docker-compose
   ```

### 2. Decompress the Directory
Unpack the sent directory:
   ```bash
   tar -xzvf pegelhub.tar.gz
   ```

### 3. Clean Up
Remove the compressed file:
   ```bash
   rm pegelhub.tar.gz
   ```

### 4. Navigate to the Deployment Directory
Navigate to the directory containing the deployment files:
   ```bash
   cd directory_to_compress
   ```

### 5. Set File Permissions
Make the init-influxdb.sh script executable:
   ```bash
   chmod a+x init-influxdb.sh
   ```

### 6. Load Docker Images
Load the Docker images:
   ```bash
   docker load -i registry.tar
   docker load -i starter.tar
   docker load -i proxy.tar
   ```

### 7. Launch Containers
Start all the containers using Docker Compose:
   ```bash
   docker-compose --env-file testenvironment.vars up --build -d
   ```