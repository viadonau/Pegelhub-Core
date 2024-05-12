package com.stm.pegelhub.connector.tstp.parsing.impl;

import com.stm.pegelhub.connector.tstp.parsing.TstpXmlParser;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlGetResponse;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlQueryResponse;
import com.stm.pegelhub.lib.model.Measurement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TstpXmlParserImpl implements TstpXmlParser {

    public List<Measurement> parseXmlGetToMeasurements(String xml) {
        XmlGetResponse responseObject = unmarshalXml(xml);
        String[] rawMeasurements = extractRawMeasurements(responseObject);

        return parseRawMeasurements(rawMeasurements);
    }

    public XmlQueryResponse parseXmlCatalog(String xmlCatalog) {
        return unmarshalXmlCatalog(xmlCatalog);
    }

    private XmlQueryResponse unmarshalXmlCatalog(String xmlCatalog) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlQueryResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xmlCatalog);

            return (XmlQueryResponse) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException("There was an error unmarshalling the XML Catalog");
        }
    }

    private List<Measurement> parseRawMeasurements(String[] rawMeasurements) {
        List<Measurement> measurements = new ArrayList<>();

        for (String rawMeasurement : rawMeasurements) {
            String[] timestampAndMeasurement = rawMeasurement.split(" ");

            if (timestampAndMeasurement.length != 2) {
                System.err.println("Wrong number of arguments in a raw measurement");
                continue;
            }

            try {
                LocalDateTime timestamp = parseTimestampToLocalDateTime(timestampAndMeasurement[0]);
                Double measurementValue = Double.parseDouble(timestampAndMeasurement[1]);

                HashMap<String, Double> valueMap = new HashMap<>();
                valueMap.put("value", measurementValue);

                measurements.add(new Measurement(timestamp, valueMap, new HashMap<>()));
            } catch (Exception e) {
                System.err.println("Raw measurement has a wrong format");
            }
        }

        return measurements;
    }

    private String[] extractRawMeasurements(XmlGetResponse xmlGetResponse) {
        String rawData = xmlGetResponse.getData();
        String[] m = rawData.split("\\[")[2].split("\n");
        m[m.length - 1] = m[m.length - 1].replace("]]", "");

        return m;
    }

    private XmlGetResponse unmarshalXml(String xml) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlGetResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);

            return (XmlGetResponse) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException("There was an error unmarshalling the XML");
        }
    }

    private LocalDateTime parseTimestampToLocalDateTime(String timestamp) {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
    }
}