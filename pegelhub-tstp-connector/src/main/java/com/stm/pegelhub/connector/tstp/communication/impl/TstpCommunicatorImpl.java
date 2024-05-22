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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class TstpCommunicatorImpl implements TstpCommunicator {
    private static final Logger LOG = LoggerFactory.getLogger(TstpCommunicatorImpl.class);
    private final String baseURI;
    private final HttpClient httpClient;
    private final TstpXmlService tstpXmlService;
    private final String userAndPassword;

    public TstpCommunicatorImpl(String tstpAddress, int tstpPort, String userAndPassword, HttpClient httpClient, TstpXmlService tstpXmlService) {
        this.baseURI = "http://"+tstpAddress+":"+tstpPort+"/?Cmd=";
        this.httpClient = httpClient;
        this.tstpXmlService = tstpXmlService;
        this.userAndPassword = userAndPassword;
    }

    public List<Measurement> getMeasurements(String zrid, Instant readFrom, Instant readUntil, String quality) {
        URI uri = URI.create(String.format(baseURI+"Get&ZRID=XKNg1sGkbQnpWLTZezsPgA&Von=2010-08-03T13:30:00Z&Bis=2010-08-03T20:30:00Z"));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        try {
            String responseBody = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            return tstpXmlService.parseXmlGetResponseToMeasurements(responseBody);
        } catch (Exception e) {
            System.err.println("Could not get a response from the TSTP-Server");
            return new ArrayList<>();
        }
    }


    public XmlQueryResponse getCatalog() {
        URI uri = URI.create(String.format(baseURI+"Query&Parameter=Wasserstand&Ort=10001373&DefArt=K'"));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        try {
            String responseBody = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            return tstpXmlService.parseXmlCatalog(responseBody);
        } catch (Exception e) {
            System.err.println("Could not get a response from the TSTP-Server");
            return null;
        }
    }

    @Override
    public void sendMeasurements(String zrid, List<Measurement> measurements) {
        URI uri = URI.create(String.format(baseURI+"PUT&ZRID=%s&Qual=%s",zrid, "1"));
        String requestBody = tstpXmlService.parseXmlPutRequest(measurements);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Authorization", "Basic "+base64Encode(userAndPassword))
                .build();

        try {
            String responseBody = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            XmlTsResponse response = tstpXmlService.parseXmlPutResponse(responseBody);
            if(response.getMessage().contains("confirm")){
                LOG.info("Successfully sent data to the TSTP-Server");
            } else {
                LOG.info("There was an error sending the data: "+response.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Could not get a response from the TSTP-Server");
        }
    }

    private String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
}
