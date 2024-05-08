package com.stm.pegelhub.connector.tstp.xmlparsing;

import com.stm.pegelhub.connector.tstp.xmlparsing.implementation.XmlParser;

public class ParserFactory {
    public static Parser getParser(ConnectorMode type) {
        return switch (type) {
            case WRITE -> new XmlParser();
            case READ -> null;
            default -> throw new IllegalStateException("Undefined parser!");
        };
    }
}
