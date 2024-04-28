package com.stm.pegelhub.connector.tstp.xmlparsing;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public interface Parser {
    Method getType();
    Stream<Entry> parse(InputStream is) throws IOException;
}