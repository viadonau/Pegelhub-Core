package com.stm.pegelhub.connector.tstp.communication.impl;

import com.stm.pegelhub.connector.tstp.communication.TstpCommunicator;
import com.stm.pegelhub.connector.tstp.parsing.TstpXmlParser;
import com.stm.pegelhub.connector.tstp.parsing.impl.TstpXmlParserImpl;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlQueryResponse;
import com.stm.pegelhub.lib.model.Measurement;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TstpCommunicatorImpl implements TstpCommunicator {
    private final String baseURI;
    private final HttpClient client;
    private final TstpXmlParser tstpXmlParser;
    private final String encodedUserAndPassword;

    public TstpCommunicatorImpl(String tstpAddress, int tstpPort, String encodedUserAndPassword) {
        this.baseURI = "http://"+tstpAddress+":"+tstpPort+"/?Cmd=";
        this.client = HttpClient.newHttpClient();
        this.tstpXmlParser = new TstpXmlParserImpl();
        this.encodedUserAndPassword = encodedUserAndPassword;
    }

    public List<Measurement> getMeasurements(String zrid, Instant readFrom, Instant readUntil, String quality) {
        URI uri = URI.create(String.format(baseURI+"Get&ZRID=%s&Von=%s&Bis=%s&Qual=%s", zrid, readFrom.toString(), readUntil.toString(), quality));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Basic "+encodedUserAndPassword)
                .build();

        try {
            String responseBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            return tstpXmlParser.parseXmlGetToMeasurements(responseBody);
        } catch (Exception e) {
            System.err.println("Could not get a response from the TSTP-Server");
            return new ArrayList<>();
        }
    }


    //change arguments
    public XmlQueryResponse getCatalog(Instant readFrom, Instant readUntil, String quality) {
        URI uri = URI.create(String.format(baseURI+"Query&ZRID=%d&Von=%s&Bis=%s&Qual=%s",123, readFrom.toString(), readUntil.toString(), quality));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Basic "+encodedUserAndPassword)
                .build();

        try {
            String responseBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            return tstpXmlParser.parseXmlCatalog(responseBody);
        } catch (Exception e) {
            System.err.println("Could not get a response from the TSTP-Server");
            return null;
        }
    }
}
