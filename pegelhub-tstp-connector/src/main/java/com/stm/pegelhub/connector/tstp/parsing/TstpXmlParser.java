package com.stm.pegelhub.connector.tstp.parsing;

import com.stm.pegelhub.connector.tstp.parsing.model.XmlQueryResponse;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlTsResponse;
import com.stm.pegelhub.lib.model.Measurement;

import java.util.List;

/**
 * Handles the parsing for the TSTP-XML structure
 */
public interface TstpXmlParser {
    /**
     * Parses the XMLGetResponse from the TSTP-Server to a list of measurements
     *
     * @param responseBody the response from the server
     * @return the parsed measurements
     */
    List<Measurement> parseXmlGetResponseToMeasurements(String responseBody);

    /**
     * Parses the raw query response (zrid catalog) from the TSTP-Server to a XmlQueryResponse object
     *
     * @param xmlCatalog the xml catalog response from the server
     * @return the parsed XmlQueryResponse object
     */
    XmlQueryResponse parseXmlCatalog(String xmlCatalog);

    /**
     * Parses the raw put response from the TSTP-Server to a XmlTsResponse object
     *
     * @param responseBody the response from the server
     * @return the parsed XmlTsResponse object
     */
    XmlTsResponse parseXmlPutResponse(String responseBody);

    /**
     * Parses a list of measurements from the core to a TSTP PUT request structure
     *
     * @param measurements the measurements from the core
     * @return the parsed XmlPutRequest object
     */
    String parseXmlPutRequest(List<Measurement> measurements);
}
