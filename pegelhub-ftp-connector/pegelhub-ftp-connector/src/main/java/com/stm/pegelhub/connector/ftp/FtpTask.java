package com.stm.pegelhub.connector.ftp;

import com.stm.pegelhub.connector.ftp.fileparsing.Entry;
import com.stm.pegelhub.connector.ftp.fileparsing.Parser;
import com.stm.pegelhub.lib.InfluxID;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.internal.ApplicationProperties;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesImpl;
import com.stm.pegelhub.lib.model.Measurement;
import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.net.InetAddress;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FtpTask extends TimerTask {
    private static final Logger LOG = LoggerFactory.getLogger(FtpTask.class);
    private final HashSet<String> processedFiles = new HashSet<>();
    private final Duration durationToLookBack;
    private final FTPClient ftp;
    private final ConnectorOptions conOpts;
    private final PegelHubCommunicator communicator;
    private final Parser parser;
    private InfluxID influxID;
    private ApplicationProperties properties;

    public FtpTask(FTPClient ftp, ConnectorOptions conOpts, PegelHubCommunicator communicator, Parser parser) {
        this.ftp = ftp;
        this.conOpts = conOpts;
        this.communicator = communicator;
        this.parser = parser;
        this.properties = new ApplicationPropertiesImpl(conOpts.propertiesFile());
        this.influxID = new InfluxID(communicator, properties);
        this.durationToLookBack = Duration.ofHours(50000); //conOpts.readDelay().plus(conOpts.readDelay());
    }

    /**
     * The connection to the FTP Server. Reads the file and tries to parse it. If successful, the parsed Measurements get
     * transferred to pegelhub-core
      */
    @Override
    public void run(){
        try {
            ftp.connect(conOpts.pegelAddress().getHostAddress(), conOpts.pegelPort());
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
            conf.setUnparseableEntries(true);
            ftp.configure(conf);
            ftp.setDefaultPort(21);
            int replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                throw new RuntimeException("FTP couldn't connect!");
            }
            if (!ftp.login(conOpts.username(), conOpts.password())) {
                ftp.disconnect();
                throw new RuntimeException("Unable to login!");
            }
        } catch (IOException e) {
            LOG.error("Couldn't login to FTP!", e);
            return;
        }

        try {
            ftp.enterLocalPassiveMode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            // 1) list files
            // 2) process files
            // 3) send processed data to core

            // 1)

            FTPFileFilter filter = file -> file != null
                        && file.isFile()
                        && file.getName() != null
                        && file.getTimestampInstant() != null
                        && file.getName().endsWith(parser.getType().fileSuffix)
                        && file.getTimestampInstant().isAfter(Instant.now().minus(durationToLookBack))
                        && !processedFiles.contains(file.getName());

            FTPFile[] files;
            try {
                System.out.println(conOpts.path());
                files = ftp.listFiles(conOpts.path());


            } catch (IOException e) {
                LOG.error("Can't list files!", e);
                return;
            }

            List<Measurement> measurements = null;

            // 2)

                measurements = Arrays.stream(files)
                        .flatMap(this::parseFile)
                        .flatMap(this::convertEntryToMeasurementStream)
                        .collect(Collectors.toList());


            // 3)
            if (!measurements.isEmpty()) {
                communicator.sendMeasurements(measurements);
            }
        } catch (Exception e) {
            LOG.error("Unhandled Exception was thrown!", e);
        } finally {
            try {
                ftp.logout();
            } catch (IOException e) {
                LOG.error("Couldn't logout of FTP!", e);
            }
            try {
                ftp.disconnect();
            } catch (IOException e) {
                LOG.error("Couldn't disconnect!", e);
            }
        }
    }

    private Stream<Entry> parseFile(FTPFile file) {
        System.out.println(file.getName());
        final String formatString = conOpts.path().endsWith("/") ? "%s%s" : "%s/%s";
        final String fileLocation = String.format(formatString, conOpts.path(), file.getName());
        System.out.println(fileLocation);
        InputStream fileStream = getFileInputStream(fileLocation);

        Stream<Entry> returnvalue = Stream.of();
        if (fileStream != null) {

            processedFiles.add(file.getName());
            try {
                returnvalue = parser.parse(fileStream);
                ftp.completePendingCommand();
            } catch (IOException e) {
                LOG.error("Error while reading file!", e);
            }
        }
            processedFiles.remove(file.getName());
            try {
                fileStream.close();
            } catch (Exception e) {
                LOG.error("Stream did not close", e);
            }
        return returnvalue;
    }

    private InputStream getFileInputStream(String location) {
        InputStream fileStream;
        try {
            String[] locations = location.split("/");
            System.out.println(location);
            fileStream = ftp.retrieveFileStream(location);
            if (fileStream == null) {
                LOG.error("Couldn't open filestream for \"{}\". No exception was thrown, but filestream is null.", location);
                return null;
            }
        } catch (IOException e) {
            LOG.error(String.format("IOException was thrown while opening filestream for \"%s\"!", location), e);
            return null;
        }
        return fileStream;
    }

    private Stream<Measurement> convertEntryToMeasurementStream(Entry e) {
        return e.getValues().entrySet().stream().map(value -> {
            if (!Util.canParseDouble(value.getValue())) {
                return null;
            }
            influxID.calculateID();
            var m = new Measurement();
            m.setTimestamp(value.getKey().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            m.getFields().put("value", Double.parseDouble(value.getValue()));
            m.getInfos().putAll(e.getInfos());
            m.getInfos().put("ID", String.valueOf(influxID.getIDValue()));
            influxID.addID();
            return m;
        }).filter(Objects::nonNull);
    }
}
