package com.stm.pegelhub.connector.tstp.parsing.impl;

import com.stm.pegelhub.connector.tstp.parsing.TstpXmlParser;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlTsData;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlQueryResponse;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlTsDefinition;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlTsResponse;
import com.stm.pegelhub.lib.model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class TstpXmlParserImpl implements TstpXmlParser {
    private static final Logger LOG = LoggerFactory.getLogger(TstpXmlParserImpl.class);
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
        int byteArraySize = measurements.size() * 12;
        byte[] binaryBlock = new byte[byteArraySize];

        for(int i = 0; i < measurements.size(); i++){
            Measurement currentMeasurement = measurements.get(i);
            LocalDateTime timestamp = currentMeasurement.getTimestamp();
            float measurementValue = (float) currentMeasurement.getFields().get("value").doubleValue();

            byte[] dateBytes = new byte[8];
            // Byte 7
            dateBytes[0] = (byte) 0;
            // Byte 6
            dateBytes[1] = (byte)((timestamp.getYear() >> 7) & 0x0F);
            // Byte 5
            dateBytes[2] = (byte)(timestamp.getYear() & 0xFF);
            // Byte 4
            dateBytes[3] = (byte)(timestamp.getMonthValue() & 0x0F);
            // Byte 3
            dateBytes[4] = (byte)(timestamp.getDayOfMonth() & 0x0F);
            // Byte 2
            dateBytes[5] = (byte)(timestamp.getHour() & 0xFF);
            // Byte 1
            dateBytes[6] = (byte)(timestamp.getMinute() & 0xFF);
            // Byte 0
            dateBytes[7] = (byte)(timestamp.getSecond() & 0xFF);

            // float to int bits
            int bits = Float.floatToIntBits(measurementValue);
            byte[] floatBytes = ByteBuffer.allocate(4).order(java.nio.ByteOrder.BIG_ENDIAN).putInt(bits).array();

            // 12 bytes ( 8 date bytes + 4 value bytes)
            int copyToPosistion = i * 12;
            System.arraycopy(dateBytes, 0, binaryBlock, copyToPosistion, 8);
            System.arraycopy(floatBytes, 0, binaryBlock, copyToPosistion + 8, 4);
        }

        String binaryEncoded = insertNewlines(Base64.getEncoder().encodeToString(binaryBlock));

        XmlTsDefinition xmlTsDef = new XmlTsDefinition("Z", "Nein", "K", "cm", "0", String.valueOf(measurements.size()));
        XmlTsData xmlTsData = new XmlTsData(xmlTsDef, binaryEncoded);

        var parsingTest = parseXmlTsDataToMeasurementList(xmlTsData);

        return xmlTsData;
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
        List<Measurement> measurementList = parseDataFromBinary(decoded);

        return measurementList;
    }

    private List<Measurement> parseDataFromBinary(byte[] decoded) {
        List<Measurement> measurementList = new ArrayList<>();

        for(int j = 0; j < decoded.length; j = j+12) {
            byte[] dateBytes = Arrays.copyOfRange(decoded, j, j+8);

            int year = 0;
            int month = 0;
            int day = 0;

            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            for(int k = 0; k < dateBytes.length; k++){
                byte dateByte = dateBytes[k];

                //comments are referring the table 1 from the tstp documentation
                //Byte 6
                if(k == 1){
                    for(int i = 3; i >= 0; i--){
                        int bit = getBit(dateByte, i);
                        year = (year << 1) | bit;
                    }
                }
                //Byte 5
                if(k == 2){
                    for(int i = 7; i >= 0; i--){
                        int bit = getBit(dateByte, i);
                        year = (year << 1) | bit;
                    }
                }
                //Byte 4
                if(k == 3){
                    for(int i = 3; i >= 0; i--){
                        int bit = getBit(dateByte, i);
                        month = (month << 1) | bit;
                    }
                }
                //Byte 3
                if(k == 4){
                    for(int i = 3; i >= 0; i--){
                        int bit = getBit(dateByte, i);
                        day = (day << 1) | bit;
                    }
                }

                //Byte 2
                if(k == 5){
                    hours = (hours << 8) | (dateByte & 0xFF);
                }
                //Byte 1
                if(k == 6){
                    minutes = (minutes << 8) | (dateByte & 0xFF);
                }
                //Byte 0
                if(k == 7){
                    seconds = (seconds << 8) | (dateByte & 0xFF);
                }
            }
            int bits = ByteBuffer.wrap(Arrays.copyOfRange(decoded, j+8, j+12)).order(java.nio.ByteOrder.BIG_ENDIAN).getInt();
            double ieeeFloat = Float.intBitsToFloat(bits);

            LocalDateTime dateTime = LocalDateTime.of(year, month, day, hours, minutes, seconds);

            LocalDateTime mockTimeForTesting = LocalDateTime.of(LocalDate.now().minusDays(1), dateTime.toLocalTime());
            HashMap<String, Double> valueMap = new HashMap<>();
            valueMap.put("value", ieeeFloat);
            LOG.info("Parsed measurement: "+mockTimeForTesting+", "+ieeeFloat);
            measurementList.add(new Measurement(mockTimeForTesting, valueMap, new HashMap<>()));
        }
        return measurementList;
    }

    private int getBit(byte in, int position) {
        return (in >> position) & 1;
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
}
