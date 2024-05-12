package com.stm.pegelhub.connector.tstp.parsing;

import com.stm.pegelhub.connector.tstp.parsing.model.XmlQueryResponse;
import com.stm.pegelhub.lib.model.Measurement;

import java.util.List;

public interface TstpXmlParser {
    List<Measurement> parseXmlGetToMeasurements(String xml);
    XmlQueryResponse parseXmlCatalog(String xmlCatalog);
}
