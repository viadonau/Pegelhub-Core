package com.stm.pegelhub.connector.tstp.xmlparsing.implementation;

import com.stm.pegelhub.connector.tstp.xmlparsing.ConnectorMode;
import com.stm.pegelhub.connector.tstp.xmlparsing.Entry;
import com.stm.pegelhub.connector.tstp.xmlparsing.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class XmlParser implements Parser {

    @Override
    public ConnectorMode getType() {
        return null;
    }

    @Override
    public Stream<Entry> parse(InputStream is) throws IOException {
        //do parsing logik here
        List<Entry> entries = new ArrayList<>();

        return entries.stream();
    }
}
