# Postman Collection: pegelhub

This Postman collection contains a set of API requests for the pegelhub core.

<br>
<br>

## Usage Instructions
__________________________________________________________________________________________

1. Install [Postman](https://www.postman.com/downloads/), if you haven't already.
2. Import the collection.
3. Run the pegelhub core.
4. Set the required variables in the collection or environment to match your specific setup.
    * `baseAddress`: The IP Address of your pegelhub core instance.
    * `port`: The port of the pegelhub core application.
    * `apiPath`: The api path of your core application, which only needs to be modified if you actively changed the api path in the pegelhub core application.


<br>
<br>


## Collection Structure
__________________________________________________________________________________________

### Metadata

- **Token**
  - **Refresh Token**: Sends a PUT request to refresh the token.
  - **Create Token**: Sends a POST request to create a new token, depending on a given type.
  - **Delete Token**: Sends a DELETE request to invalidate the token.
  - **Get TokenIds**: Sends a GET request to retrieve all token IDs.

- **Supplier**
    - **Get All**: Sends a GET request to retrieve all suppliers.
    - **Get All With Measurement**: Sends a GET request to retrieve all suppliers with their respective last measurement.
    - **Get by Id**: Sends a GET request to retrieve a supplier by UUID.
    - **Create Supplier**: Sends a POST request to create a new supplier.
    - **Delete Supplier**: Sends a DELETE request to delete a supplier.

### Data
- **Measurement**
    - **Get Measurement in Range**: Sends a GET request to retrieve all measurements within a range.
    - **Measurement by Supplier**: Sends a GET request to retrieve all measurements by a  supplier within a range.


<br>
<br>

## Executing Requests
__________________________________________________________________________________________

You can execute the requests individually or run them as part of a sequence.

Please note that the requests in this collection might require specific authentication or authorization credentials based on the application's requirements. 
The system comprises three distinct levels of authorization (create, read, and write), which have been implemented using API Keys.
Upon the initial launch of Pegelhub, a default API key with create privileges is automatically generated and can be found at [PegelHub README](../README.md). 
This deafult key has already been assigned to the createApiKey variable within the collection. 
Furthermore, by utilizing the create key, it is possible to request read and write keys as well: Execute a CreateToken request, inserting either "read" or "write" in the type variable.


Moreover, when dealing with certain requests, it is essential to utilize the values of database objects. 
herefore, ensure that you employ valid values that exist within your database.
- **Get by Id**: Requires the UID of a supplier.
- **Delete Supplier**: Requires the UID of a supplier.
- **Get Measurement in Range**: Requires the range of a measurement.
- **Measurement by Supplier**: Requires the range an supplier sof a measurement.



For more information on each request's details and parameters, refer to the request documentation within the collection.



