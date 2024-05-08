package com.stm.pegelhub.connector.tstp;

import com.stm.pegelhub.connector.tstp.xmlparsing.Entry;
import com.stm.pegelhub.connector.tstp.xmlparsing.Parser;
import com.stm.pegelhub.lib.InfluxID;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.internal.ApplicationProperties;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesImpl;
import com.stm.pegelhub.lib.model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TstpTask extends TimerTask {
    private static final Logger LOG = LoggerFactory.getLogger(TstpTask.class);
    private final HashSet<String> processedFiles = new HashSet<>();
    private final Duration durationToLookBack;
    // some tstp communicator
    private final ConnectorOptions conOpts;
    private final PegelHubCommunicator communicator;
    private final Parser parser;
    private InfluxID influxID;
    private ApplicationProperties properties;

    public TstpTask(ConnectorOptions conOpts, PegelHubCommunicator communicator, Parser parser) {
        this.conOpts = conOpts;
        this.communicator = communicator;
        this.parser = parser;
        this.properties = new ApplicationPropertiesImpl(conOpts.propertiesFile());
        this.influxID = new InfluxID(communicator, properties);
        this.durationToLookBack = conOpts.readDelay();
    }

    /**
     * The connection to the FTP Server. Reads the file and tries to parse it. If successful, the parsed Measurements get
     * transferred to pegelhub-core
      */
    @Override
    public void run(){
        // connect to tstp server

        // 1) list files
        // 2) process files
        // 3) send processed data to core

        try {
            // 1)
            String[] files = new String[1];
            //get files

            // 2)
            List<Measurement> measurements = Arrays.stream(files)
                    .flatMap(this::parseXmlToEntry)
                    .flatMap(this::convertEntryToMeasurementStream)
                    .collect(Collectors.toList());

            // 3)
            if (!measurements.isEmpty()) {
                communicator.sendMeasurements(measurements);
            }
        } catch (Exception e) {
            LOG.error("Unhandled Exception was thrown!", e);
        } finally {
            // disconnect tstp if needed
        }
    }

    private Instant getLookBackTimestamp() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.systemDefault());
        return currentTime.minus(durationToLookBack)
                .minus(Duration.of(currentTime.getOffset().getTotalSeconds(), ChronoUnit.SECONDS))
                .toInstant();
    }

    private Stream<Entry> parseXmlToEntry(String file) {
        System.out.println(file);
        InputStream fileStream = getXmlInputStream();

        Stream<Entry> returnvalue = Stream.of();
        if (fileStream != null) {

            processedFiles.add(file);
            try {
                returnvalue = parser.parse(fileStream);
            } catch (IOException e) {
                LOG.error("Error while reading file!", e);
            }
        }
            processedFiles.remove(file);
            try {
                fileStream.close();
            } catch (Exception e) {
                LOG.error("Stream did not close", e);
            }
        return returnvalue;
    }

    private InputStream getXmlInputStream() {
        InputStream fileStream;
        try {
            // get body xml from response i guess?
            fileStream = new FileInputStream("asdf");
            if (fileStream == null) {
                LOG.error("Couldn't open filestream for \"{}\". No exception was thrown, but filestream is null.", "location");
                return null;
            }
        } catch (IOException e) {
            LOG.error(String.format("IOException was thrown while opening filestream for \"%s\"!", "location"), e);
            return null;
        }
        return fileStream;
    }

    private Stream<Measurement> convertEntryToMeasurementStream(Entry e) {
        return e.getValues().entrySet().stream().map(value -> {
            if (!Util.canParseDouble(value.getValue())) {
                return null;
            }
            var m = new Measurement();
            m.setTimestamp(value.getKey().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            m.getFields().put("value", Double.parseDouble(value.getValue()));
            m.getFields().put("ID", (double) influxID.getIDValue());
            m.getInfos().putAll(e.getInfos());
            influxID.addID();
            return m;
        }).filter(Objects::nonNull);
    }
}
