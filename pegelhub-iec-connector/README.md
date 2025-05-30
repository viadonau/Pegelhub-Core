
# IEC Connector

The IEC Connector enables the exchange of telemetry and measurement data using the IEC 60870-5-104 transport standard.


## Resources Used

The IEC Connector module utilizes the following library:
* [openmuc j60870](https://www.openmuc.org/j60870-release-1-5-0/): A library that provides support for the IEC 60870-5-104 protocol.

Documentation:
* [Beckhoff TF6500](https://infosys.beckhoff.com/english.php?content=../content/1033/tf6500_tc3_iec60870_5_10x/984065803.html&id=9038877514577555054): Offers support for IEC 60870-5-104 communication.


## Usage Instructions

### Configuration

Follow the instructions below to set up the IEC Connector, so it can be started with Docker:

1. Make sure your Pegelhub-Core is running in Docker.
2. Copy the  ```config.properties ``` and  ```properties.yaml ``` into the folder  ```iec_directory ``` in the pegelhub-iec-connector. If the folder does not exist yet, create it.
3. Modify the example config-file ```config.properties``` to personalize the configuration.
    * Set the ```Connector.IsReadingFromIec``` value to "true" to enable reading, otherwise the connector is performing writing operations.
    * Adjust the ```DelayInterval``` to specify the desired time interval for reading or writing operations, respectively.
    * Configure the standard specific properties, such as ```Iec.CommonAddress``` and ```Iec.StartDtRetries```, to set the port, host, and other necessary parameters for the IEC server.
    * Set the ```StationNumbers``` property to specify the list of station numbers to interact with. Separate multiple station numbers with commas.


4. Set the required data in the ```properties.yaml```  file for the IEC Hub library's configuration as a supplier. This includes generating a new API token:
    * To generate a new token, open Postman.
    * In this project, open the folder ```postman-collection``` and import the file ```pegelhub.postman_collection.json``` into Postman.
    * In Postman, click on the main folder (pegelhub) and on Variables. Change the port of your Pegelhub installation if necessary.
    * Go to Token - POST Create Token and insert ```{{baseAddress}}:{{port}}/{{apiPath}}/token?apiKey={{createApiKey}}&type=read&description=Description``` into the empty field on the top.
    * Make sure to use either type=read or type=write, depending on whether the token is for reading or writing operations.
    * Click send -> an API token should now appear in the response field.


5. Go back to your terminal and navigate into the right directory with ```cd pegelhub-iec-connector```
6. Run the command ```mvn clean package```
    * In case there are problems with the unit tests, use ```mvn clean package -DskipTests``` instead
7. Build the container with ```docker build -t iec-connector .```
8. Then, start the container with ```docker run --name iec-connector -d -v .\iec_directory:/app/files iec-connector```


## Testing

### TestPegel

The ```TestPegel``` represents the IEC sample server used for testing purposes. You can query data from this server.

In the [SampleServer.java](./src/main/java/TestPegel/SampleServer.java) class you can find the IEC Test Server which is only for testing.  
You only need to start the [SampleServer.java](./src/main/java/TestPegel/SampleServer.java) in your IDE and you will get a protocol on your console and can see the Bind Address, Port, IAO length, CA length and the COT length. Therefore the Test worked.  
It is also possible to change the settings for what you want/need in the test server.

### FreyrScada IEC Test-Server
This is a Testserver which can be found on Github: https://github.com/FreyrSCADA/IEC-60870-5-104
* After Installation, click on "Add Server" on the top left.
* Switch to the tab "Configuration"
* Click "Add Row" and click on "Choose a group", select a group from the dropdown menu.
* Click "Load Configuration"
* Change to tab "Data_Objects" and click "Start Communication"


