package com.stm.pegelhub.connector.tstp.communication.impl;

import com.stm.pegelhub.connector.tstp.communication.TstpCommunicator;
import com.stm.pegelhub.connector.tstp.service.TstpXmlService;
import com.stm.pegelhub.connector.tstp.service.model.XmlQueryResponse;
import com.stm.pegelhub.connector.tstp.service.model.XmlTsResponse;
import com.stm.pegelhub.lib.model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TstpCommunicatorImpl implements TstpCommunicator {
    private static final Logger LOG = LoggerFactory.getLogger(TstpCommunicatorImpl.class);
    private final String baseURI;
    private final HttpClient httpClient;
    private final TstpXmlService tstpXmlService;

    public TstpCommunicatorImpl(String tstpAddress, int tstpPort, HttpClient httpClient, TstpXmlService tstpXmlService) {
        this.baseURI = "http://"+tstpAddress+":"+tstpPort+"/?Cmd=";
        this.httpClient = httpClient;
        this.tstpXmlService = tstpXmlService;
    }

    public List<Measurement> getMeasurements(String zrid, Instant readFrom, String readUntil) {
        URI uri = URI.create(String.format(baseURI+"Get&ZRID=%s&Von=%s&Bis=%s", zrid, formatInstant(readFrom), readUntil));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        try {
            LOG.debug("Get Request:");
            LOG.debug(uri.toString());
            String responseBody = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            LOG.debug("Response body:");
            LOG.debug(responseBody);
            return tstpXmlService.parseXmlGetResponseToMeasurements(responseBody);
        } catch (Exception e) {
            LOG.info("Could not get a response from the TSTP-Server");
            return new ArrayList<>();
        }
    }


    public XmlQueryResponse getCatalog(int dbms) {
        URI uri = URI.create(String.format(baseURI+"Query&ORT=%d&Parameter=Wasserstand&Hauptreihe=true", dbms));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        try {
            LOG.info("catalog request:");
            LOG.info(uri.toString());
            String responseBody = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            LOG.info("response:");
            LOG.info(responseBody);
            return tstpXmlService.parseXmlCatalog(responseBody);
        } catch (Exception e) {
            LOG.info("Could not get a response from the TSTP-Server");
            return null;
        }
    }

    @Override
    public void sendMeasurements(String zrid, List<Measurement> measurements) {
        URI uri = URI.create(String.format(baseURI+"PUT&ZRID=%s",zrid));
        String requestBody = tstpXmlService.parseXmlPutRequest(measurements);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            String responseBody = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            XmlTsResponse response = tstpXmlService.parseXmlPutResponse(responseBody);
            if(response.getMessage().contains("confirm")){
                LOG.info("Successfully sent data to the TSTP-Server");
                LOG.info(response.getMessage());
            } else {
                LOG.info("There was an error sending the data: "+ response.getMessage());
                LOG.info(responseBody);
            }
        } catch (Exception e) {
            LOG.info("Could not get a response from the TSTP-Server");
        }
    }

    private String formatInstant(Instant toFormat) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);
        return dtf.format(toFormat);
    }
}
