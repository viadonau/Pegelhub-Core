# Inter-Cluster-Communication Connector

This connector is used for syncing data of selected suppliers between two PegelHub clusters.

## Setup

1) Build the PegelHub-Library Project:
   * Open the project inside of `../pegelhub-library`
   * Execute the goal `mvn clean`
   * Execute the goal `mvn package`
   * Execute the goal `mvn install`

## How to use

Read the readme in the postman collection folder on how to generate an api token.
`apiToken`s cannot be reused for applications using the API-Client. It must be unique for each connector instance.

1) Do the Setup
2) Start two PegelHub clusters:
   * The two clusters must have their unique HTTP(S) addresses
   * They don't need to run on separate machine
3) Acquire the HTTP(S) addresses of the two PegelHub instances
4) Open `config.properties`:
   * Set `Core.Source` as the HTTP(S) address of the PegelHub where data is fetched from
   * Set `Icc.SourceStationNumber` as the station numbers of the supplier station numbers whose data should be synced with the `Core.Sink`, separated using `,` (e.g. `ST1,ST3,Station6`)
   * Set `Core.Sink` as the HTTP(S) address of the PegelHub where data is sent to
   * Set the desired interval for `Icc.RefreshInterval`:
     * Supported formats are: 
       * `24H` or `24h` => every 24 hours
       * `20M` or `20m` => every 20 minutes
       * `45S` or `45s` => every 45 seconds
     * These cannot be combined (e.g. `24H20M` is not valid!)
5) Open `source_properties.yaml`:
   * Generate and activate new token in the PegelHub instance that is set as the `Core.Source`
   * Set this token as `apiToken`
   * Make sure `isSupplier` is set to `false`
   * If there is a node `supplier`, remove it
   * Configure everything below `taker` to your liking
      * `taker.connector.number` and `taker.id` must be unique within a PegelHub cluster 
6) Open `sink_properties.yaml`:
   * Generate and activate new token in the PegelHub instance that is set as the `Core.Sink`
   * Set this token as `apiToken`
   * Make sure `isSupplier` is set to `true`
   * If there is a node `taker`, remove it
   * Configure everything below `supplier` to your liking
     * `supplier.connector.number` and `supplier.id` must be unique within a PegelHub cluster
7) Start this project/Build the jar