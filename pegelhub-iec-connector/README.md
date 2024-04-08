# IEC Connector

The IEC Connector enables the exchange of telemetry and measurement data using the IEC 60870-5-104 transport standard.

<br>
<br>

## Resources Used
________________________________

The IEC Connector module utilizes the following library:
* [openmuc j60870](https://www.openmuc.org/j60870-release-1-5-0/): A library that provides support for the IEC 60870-5-104 protocol.

Documentation:
* [Beckhoff TF6500](https://infosys.beckhoff.com/english.php?content=../content/1033/tf6500_tc3_iec60870_5_10x/984065803.html&id=9038877514577555054): Offers support for IEC 60870-5-104 communication.


<br>
<br>
## Usage Instructions

________________________________

### Configuration 

Follow the instructions below to use the IEC Connector module effectively:

1. Modify the ExampleConfig file [config.properties](./src/main/resources/config.properties) to personalize the configuration.
    * Set the ```Connector.IsReadingFromIec``` value to "true" to enable reading otherwise the connector is performing writing operations.
    * Adjust the ```DelayInterval``` to specify the desired time interval for reading or writing operations, respectively.
    * Configure the standard specific properties, such as ```Iec.CommonAddress``` and ```Iec.StartDtRetries```, to set the port, host, and other necessary parameters for the IEC server.
    * Set the ```StationNumbers``` property to specify the list of station numbers to interact with. Separate multiple station numbers with commas.


2. Set the required data in the [properties.yaml](./properties.yaml)
file for the IEC Hub library's configuration as a supplier. This includes the API token.

3. In [IecConnector.java](./src/main/java/com/stm/pegelhub/connector/iec/IecConnector.java) are some TODOs that need to be addressed:
    * Specify communication between the IEC server and the connector:
    The interrogation method is used to request data. Customize the communication based on your specific use case.
    (Note: A listener is registered to receive values transmitted from the IEC server. When the type M_ME_NB_1 is received, the data is parsed and then sent to the core.)

    * Send measurements to the IEC server. For each specified station in the configuration, measurements need to be requested and sent. Adjust the data formatting to match the target system.
    Uncomment the code block provided in IecConnector.java and map each measurement to an InformationElement.
    Use the connection.send() method to send measurements to the IEC server.


### Testing

The ```TestPegel``` represents the IEC sample server used for testing purposes. You can query data from this server.

In the [SampleServer.java](./src/main/java/TestPegel/SampleServer.java) class you can find the IEC Test Server which is only for testing.
If you did the configuration right you only need to start the [SampleServer.java](./src/main/java/TestPegel/SampleServer.java).
Then you will get a protocol on your console and can see the Bind Address, Port, IAO length, CA length and the COT length
Therefore the Test worked.
It is also possible to change the settings for what you want/need in the test server.



