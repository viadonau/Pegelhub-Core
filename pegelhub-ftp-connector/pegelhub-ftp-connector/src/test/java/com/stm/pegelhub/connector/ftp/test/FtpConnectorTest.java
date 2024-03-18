package com.stm.pegelhub.connector.ftp.test;

import com.stm.pegelhub.connector.ftp.ConnectorOptions;
import com.stm.pegelhub.connector.ftp.FtpConnector;
import com.stm.pegelhub.connector.ftp.FtpTask;
import com.stm.pegelhub.connector.ftp.fileparsing.ParserType;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.PegelHubCommunicatorFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Timer;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FtpConnectorTest {
    private ConnectorOptions conOpts;

    @BeforeEach
    public void setup() throws UnknownHostException {
        conOpts = new ConnectorOptions(InetAddress.getByName("127.0.0.1"), 8081,
                InetAddress.getByName("77.244.244.162"), 21,
                "web47ftppegelsend", "noHeshEkJernaw7",
                "/", ParserType.ASC, Duration.ofHours(2), null);
    }

    @Test
    public void createsPegelHubCommunicatorWithCorrectURL() throws Exception {
        try (var pegelhubMock = mockStatic(PegelHubCommunicatorFactory.class);
            var ftpClientMock = mockConstruction(FTPClient.class, (mock, context) -> {
                when(mock.getReplyCode()).thenReturn(200);
                when(mock.login("user", "pass")).thenReturn(true);
            });
            var timerMock = mockConstruction(Timer.class);
             var taskMock = mockConstruction(FtpTask.class)) {
            pegelhubMock.when(() -> PegelHubCommunicatorFactory.create(any())).thenReturn(mock(PegelHubCommunicator.class));

            try (var connector = new FtpConnector(conOpts)) {
                pegelhubMock.verify(() -> PegelHubCommunicatorFactory.create(eq(new URL("http://127.0.0.1:8081/"))), atLeastOnce());
            }
        }
    }

    @Test
    public void startSchedulingWithSpecifiedDelay() throws Exception {
        try (var pegelhubMock = mockStatic(PegelHubCommunicatorFactory.class);
             var ftpClientMock = mockConstruction(FTPClient.class, (mock, context) -> {
                 when(mock.getReplyCode()).thenReturn(200);
                 when(mock.login("user", "pass")).thenReturn(true);
             });
             var timerMock = mockConstruction(Timer.class);
             var taskMock = mockConstruction(FtpTask.class)) {
            pegelhubMock.when(() -> PegelHubCommunicatorFactory.create(any())).thenReturn(mock(PegelHubCommunicator.class));

            try (var con = new FtpConnector(conOpts)) {
                verify(timerMock.constructed().get(0), times(1)).scheduleAtFixedRate(any(), eq(0L), eq(Duration.ofHours(2).toMillis()));
            }
        }
    }

    @Test
    public void ftpReplyWithErrorDoesNotThrow() {
        try (var pegelhubMock = mockStatic(PegelHubCommunicatorFactory.class);
             var ftpClientMock = mockConstruction(FTPClient.class, (mock, context) -> {
                 when(mock.getReplyCode()).thenReturn(404);
             });
             var timerMock = mockConstruction(Timer.class);
             var taskMock = mockConstruction(FtpTask.class)) {
            pegelhubMock.when(() -> PegelHubCommunicatorFactory.create(any())).thenReturn(mock(PegelHubCommunicator.class));

            assertDoesNotThrow(() -> new FtpConnector(conOpts).close());
        }
    }

    @Test
    public void allMembersAreDisposedOnClose() throws Exception {
        try (var pegelhubMock = mockStatic(PegelHubCommunicatorFactory.class);
             var ftpClientMock = mockConstruction(FTPClient.class, (mock, context) -> {
                 when(mock.getReplyCode()).thenReturn(200);
                 when(mock.login("user", "pass")).thenReturn(true);
             });
             var timerMock = mockConstruction(Timer.class);
             var taskMock = mockConstruction(FtpTask.class)) {
            var pegelCommMock = mock(PegelHubCommunicator.class);
            pegelhubMock.when(() -> PegelHubCommunicatorFactory.create(any())).thenReturn(pegelCommMock);

            var con = new FtpConnector(conOpts);
            con.close();

            verify(taskMock.constructed().get(0), times(1)).cancel();
            verify(timerMock.constructed().get(0), times(1)).cancel();
            verify(pegelCommMock, times(1)).close();
            verify(ftpClientMock.constructed().get(0), times(1)).disconnect();
        }
    }
}
