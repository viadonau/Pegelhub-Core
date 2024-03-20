package com.stm.pegelhub.connector.ftp.test;

import com.stm.pegelhub.connector.ftp.ConnectorOptions;
import com.stm.pegelhub.connector.ftp.FtpTask;
import com.stm.pegelhub.connector.ftp.fileparsing.Parser;
import com.stm.pegelhub.connector.ftp.fileparsing.ParserFactory;
import com.stm.pegelhub.connector.ftp.fileparsing.ParserType;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.*;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class FtpTaskTest {
    private FTPClient client;
    private PegelHubCommunicator comm;
    private ConnectorOptions conOpts;

    @BeforeEach
    public void setup() throws UnknownHostException {
        client = new FTPClient();
        comm = mock(PegelHubCommunicator.class);
        conOpts = new ConnectorOptions(InetAddress.getByName("localhost"), 0, InetAddress.getByName("localhost"), 0, "user", "password", "", ParserType.ASC, Duration.ofHours(2), "/home/edworx/PHV03/pegelhub/pegelhub-ftp-connector/properties.yaml");
    }

    @Test
    public void nothingIsSentWhenFTPThrowsIOException() throws IOException {
        var mockClient = mock(FTPClient.class);
        when(mockClient.listFiles(any(), any())).thenThrow(new IOException());
        when(mockClient.getReplyCode()).thenReturn(200);
        when(mockClient.login(any(), any())).thenReturn(true);
        var mockParser = mock(Parser.class);
        var newTask = new FtpTask(mockClient, conOpts, comm, mockParser);

        newTask.run();

        verify(comm, never()).sendMeasurements(any());
    }

    @Test
    public void nothingIsSentWhenFTPFileStreamThrows() throws IOException {
        var mockClient = mock(FTPClient.class);
        when(mockClient.listFiles(any(), any())).thenReturn(new FTPFile[]{new FTPFile()});
        when(mockClient.retrieveFileStream(any())).thenThrow(new IOException());
        when(mockClient.getReplyCode()).thenReturn(200);
        when(mockClient.login(any(), any())).thenReturn(true);
        var mockParser = mock(Parser.class);
        var newTask = new FtpTask(mockClient, conOpts, comm, mockParser);

        newTask.run();

        verify(comm, never()).sendMeasurements(any());
    }

    @Nested
    public class TestWithRunningServer {
        private FakeFtpServer server;
        private FtpTask task;

        private static final Logger LOG = LoggerFactory.getLogger(TestWithRunningServer.class);

        @BeforeEach
        public void setup(TestInfo t) throws IOException {
            String[] tags = t.getTags().toArray(new String[0]);
            FileSystem fs = new UnixFakeFileSystem();
            fs.add(new FileEntry("/TestFile.asc", Utils.getResource(tags[0])));
            server = new FakeFtpServer();
            server.addUserAccount(new UserAccount("user", "password", "/"));
            server.setFileSystem(fs);
            server.setServerControlPort(1025);
            server.start();
            var newConOpts = new ConnectorOptions(InetAddress.getByName("localhost"), 0,
                    InetAddress.getByName("localhost"), 1025,
                    "user", "password",
                    "", ParserType.ASC, Duration.ofHours(2), "/home/edworx/PHV03/pegelhub/pegelhub-ftp-connector/properties.yaml");
            var parser = ParserFactory.getParser(newConOpts.parserType());
            task = new FtpTask(client, newConOpts, comm, parser);
        }

        @AfterEach
        public void teardown() {
            server.stop();
        }

        @Test
        @Tag("SingleEntry.asc")
        public void ftpTaskLoadsASCFileAndSendsEntriesToCommunicator() {
            task.run();
            verify(comm, atLeastOnce()).sendMeasurements(any(List.class));
        }

        @Test
        @Tag("MultipleEntries.asc")
        public void ftpTaskLoadsFileWithMultipleEntriesAndSendsEntriesToCommunicator() {
            task.run();

            verify(comm, atLeastOnce()).sendMeasurements(any(List.class));
        }

        @Test
        @Tag("GeneralError.asc")
        public void ftpTaskDoesNotThrowOnParseError() {
            assertDoesNotThrow(task::run);
        }
    }
}
