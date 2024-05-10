package com.stm.pegelhub.connector.tstp.communication;

import com.stm.pegelhub.connector.tstp.parsing.TstpParser;
import com.stm.pegelhub.lib.model.Measurement;


import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TstpCommunicator {
    private final String baseURI;
    private final HttpClient client;
    private final TstpParser tstpParser;
    private final String encodedUserAndPassword;

    public TstpCommunicator(String tstpAddress, int tstpPort, String encodedUserAndPassword) {
        this.baseURI = "http://"+tstpAddress+":"+tstpPort+"/?Cmd=";
        this.client = HttpClient.newHttpClient();
        this.tstpParser = new TstpParser();
        this.encodedUserAndPassword = encodedUserAndPassword;
    }

    public List<Measurement> getMeasurements(int zrid, Instant readFrom, Instant readUntil, String quality) {
        URI uri = URI.create(String.format(baseURI+"Get&ZRID=%d&Von=%s&Bis=%s&Qual=%s", zrid, readFrom.toString(), readUntil.toString(), quality));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Basic "+encodedUserAndPassword)
                .build();

        try {
            String responseBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            return tstpParser.parseXmlToMeasurements(responseBody);
        } catch (Exception e) {
            System.err.println("Could not get a response from the TSTP-Server");
            return new ArrayList<>();
        }
    }
}
