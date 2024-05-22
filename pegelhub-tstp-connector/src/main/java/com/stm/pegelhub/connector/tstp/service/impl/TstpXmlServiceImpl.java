package com.stm.pegelhub.connector.tstp.service.impl;

import com.stm.pegelhub.connector.tstp.service.TstpBinaryService;
import com.stm.pegelhub.connector.tstp.service.TstpXmlService;
import com.stm.pegelhub.connector.tstp.service.model.XmlTsData;
import com.stm.pegelhub.connector.tstp.service.model.XmlQueryResponse;
import com.stm.pegelhub.connector.tstp.service.model.XmlTsDefinition;
import com.stm.pegelhub.connector.tstp.service.model.XmlTsResponse;
import com.stm.pegelhub.lib.model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

public class TstpXmlServiceImpl implements TstpXmlService {
    private static final Logger LOG = LoggerFactory.getLogger(TstpXmlServiceImpl.class);
    private final TstpBinaryService binaryCodec;

    public TstpXmlServiceImpl(TstpBinaryService binaryCodec) {
        this.binaryCodec = binaryCodec;
    }

    @Override
    public List<Measurement> parseXmlGetResponseToMeasurements(String responseBody) {
        XmlTsData responseObject = unmarshalXmlTsData(responseBody);
        LOG.info("parsed ts data");

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
        byte[] binaryBlock = binaryCodec.encode(measurements);
        String binaryEncoded = insertNewlines(Base64.getEncoder().encodeToString(binaryBlock));

        XmlTsDefinition xmlTsDef = new XmlTsDefinition("Z", "Nein", "K", "cm", String.valueOf(measurements.size()*12), String.valueOf(measurements.size()));
        XmlTsData xmlTsData = new XmlTsData(xmlTsDef, binaryEncoded);
        return marshallXmlTsData(xmlTsData);
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
        String rawMeasurements = data.getData().replace("\n", "");
        byte[] decoded = Base64.getDecoder().decode(rawMeasurements);
        List<Measurement> measurementList = binaryCodec.decode(decoded);

        return measurementList;
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

    private String insertNewlines(String inputString) {
        StringBuilder sb = new StringBuilder(inputString);
        int i = 60;
        while (i < sb.length()) {
            sb.insert(i, "\n");
            i += 60 + 1; // +1 to account for new \n
        }
        return sb.toString();
    }
}
