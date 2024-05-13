package com.stm.pegelhub.connector.tstp.parsing.impl;

import com.stm.pegelhub.connector.tstp.parsing.TstpXmlParser;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlTsData;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlQueryResponse;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlTsDefinition;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlTsResponse;
import com.stm.pegelhub.lib.model.Measurement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TstpXmlParserImpl implements TstpXmlParser {
    @Override
    public List<Measurement> parseXmlGetResponseToMeasurements(String responseBody) {
        XmlTsData responseObject = unmarshalXmlTsData(responseBody);

        return parseXmlTsDataToMeasurementList(responseObject);
    }
    @Override
    public XmlQueryResponse parseXmlCatalog(String xmlCatalog) {
        return unmarshalXmlCatalog(xmlCatalog);
    }

    @Override
    public XmlTsResponse parseXmlPutResponse(String xml) {
        return unmarshalXMlTsResponse(xml);
    }

    private XmlTsResponse unmarshalXMlTsResponse(String xml) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlTsResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);

            return (XmlTsResponse) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException("There was an error unmarshalling the XmlTsResponse");
        }
    }

    @Override
    public String parseXmlPutRequest(List<Measurement> measurements) {
        XmlTsData parsedData = parseMeasurementListToXmlTsData(measurements);

        return marshallXmlTsData(parsedData);
    }

    private String marshallXmlTsData(XmlTsData tsData) {
        try {
            JAXBContext jc = JAXBContext.newInstance(XmlTsData.class);
            StringWriter sw = new StringWriter();

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(tsData, sw);

            return sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException("There was an error marshalling the XmlTsData");
        }
    }

    private XmlTsData parseMeasurementListToXmlTsData(List<Measurement> measurements) {
        StringBuilder dataBuilder = new StringBuilder();

        for(int i = 0; i < measurements.size(); i++){
            Measurement currentMeasurement = measurements.get(i);
            dataBuilder.append(currentMeasurement.getTimestamp().toString())
                    .append(" ")
                    .append(currentMeasurement.getFields().get("value"));

            if(i < measurements.size()-1){
                dataBuilder.append("\n");
            }
        }

        XmlTsDefinition xmlTsDef = new XmlTsDefinition("Z", "Nein", "K", "cm", "0", String.valueOf(measurements.size()));
        return new XmlTsData(xmlTsDef, dataBuilder.toString());
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

    private List<Measurement> parseXmlTsDataToMeasurementList(XmlTsData data) {
        String rawMeasurements = data.getData();
        String[] splitUpMeasurements = rawMeasurements.split("\n");

        return Arrays.stream(splitUpMeasurements).map(rawMeasurement -> {
            String[] timestampAndMeasurement = rawMeasurement.split(" ");

            if (timestampAndMeasurement.length != 2) {
                System.err.println("Wrong number of arguments in a raw measurement");
                return null;
            }

            try {
                LocalDateTime timestamp = parseTimestampToLocalDateTime(timestampAndMeasurement[0]);
                Double measurementValue = Double.parseDouble(timestampAndMeasurement[1]);

                HashMap<String, Double> valueMap = new HashMap<>();
                valueMap.put("value", measurementValue);

                //add infos here if needed
                return new Measurement(timestamp, valueMap, new HashMap<>());
            } catch (Exception e) {
                System.err.println("Raw measurement has a wrong format");
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }

    private XmlTsData unmarshalXmlTsData(String xml) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlTsData.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);

            return (XmlTsData) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException("There was an error unmarshalling the XML");
        }
    }

    private LocalDateTime parseTimestampToLocalDateTime(String timestamp) {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
    }
}
