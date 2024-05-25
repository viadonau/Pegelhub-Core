package com.stm.pegelhub.connector.tstp.communication.impl;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Instant;
import java.util.List;

import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpResponseFactory;
import org.apache.hc.core5.http.impl.nio.DefaultHttpResponseFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.stm.pegelhub.connector.tstp.service.impl.TstpXmlServiceImpl;
import com.stm.pegelhub.lib.model.Measurement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TstpCommunicatorImplTest {
	@Mock
	private TstpXmlServiceImpl parser;
	@Mock
	private HttpClient httpClient;
	@InjectMocks
	private TstpCommunicatorImpl communicator;

	@Test
	public void getMeasurements_timeSeriesFound_listWithValues() throws IOException, InterruptedException {
		HttpResponseFactory factory = new DefaultHttpResponseFactory();
		HttpResponse response = factory.newHttpResponse(200, "TEST");
		Measurement testMeasurement = new Measurement();

		when(httpClient.send(any(), any())).thenReturn((java.net.http.HttpResponse<Object>) response);
		when(parser.parseXmlGetResponseToMeasurements(any())).thenReturn(List.of(testMeasurement));

		List<Measurement> returned = communicator.getMeasurements("1", Instant.now(), "asdf");
		assertEquals(testMeasurement, returned.get(0));
	}

	@Test
	public void getMeasurements_timeSeriesNotFound_emptyList() {

	}




}