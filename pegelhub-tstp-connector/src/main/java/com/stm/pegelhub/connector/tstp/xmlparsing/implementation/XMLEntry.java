package com.stm.pegelhub.connector.tstp.xmlparsing.implementation;

import com.stm.pegelhub.connector.tstp.xmlparsing.Entry;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class XMLEntry implements Entry {
    private String moreAttributesHere;
    private final HashMap<Date, String> values;

    public XMLEntry() {
        values = new HashMap<>();
    }

    @Override
    public Map<Date, String> getValues() {
        return null;
    }

    @Override
    public Map<String, String> getInfos() {
        return null;
    }
}
