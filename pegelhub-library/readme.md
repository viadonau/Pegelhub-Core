# PegelHub-Library

This is a library that acts as an API-Client for the PegelHub-Core.
You can use the provided `PegelHubCommunicatorFactory` in order to create an instance of this API-Client.
The following parameters must be provided:
* `baseUrl`: HTTP(S) endpoint of the PegelHub cluster that you want to communicate with

The following parameters can be provided:
* `propertiesFile`: path to the properties file, format of the properties file must be `.yaml` (default is `properties.yaml`)
* `...Route`: override for the default endpoints of the controllers inside the PegelHub (you can most likely ignore those)

The properties file contains meta information that are required for the authentication of the application that is communicating with the PegelHub cluster.
It contains the following information:
* `apiToken`: a valid and activated api token provided by the PegelHub cluster you want to communicate with. Check out the readme of the postman collection to find out how to generate and activate a token.
* `lastTokenRefresh`: last time the current api token was refreshed by the client. The client refreshes the api token automatically on startup if a refresh of the token is deemed necessary.
* `isSupplier`: determines which endpoints are enabled/disabled
  * `true` => can only do requests for sending data
  * `false` => can only do requests for fetching data
* `supplier`: if the API-Library is used as a supplier of data to the PegelHub, the meta information of this supplier
* `taker`: if the API-Library is used as a taker of data of the PegelHub, the meta information of this taker

`apiToken`s cannot be reused for applications using the API-Client. It must be unique for each connector instance.
Both the `supplier.id`/`taker.id` and the `supplier.connector.number`/`taker.connector.number` must be unique within the PegelHub cluster they are attempting to communicate with.

## Acquiring a token

* Create a token under `/api/v1/token?apiKey=<adminToken>&type=<tokenType>`:
    * `<adminToken>`: api token that has the permission to create a token (ask the owner of the pegelhub core for that)
    * `<tokenType>`: permission for token (supplier => write, taker => read)
* Ask the core owner for a token with read/write access (supplier => write, taker => read)

Known things to improve: the token is currently not refreshed during the runtime of the library. As a workaround, periodically creating a new instance of the API-Client fixes this. Fix implementation wise would be calling

```java
if (properties.isRefreshNecessary()) {
    refreshApiKey();
}
```

in a reoccurring task.