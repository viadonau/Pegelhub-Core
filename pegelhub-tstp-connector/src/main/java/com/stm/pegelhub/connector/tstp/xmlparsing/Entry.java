package com.stm.pegelhub.connector.tstp.xmlparsing;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Entry {
    final Map<Date, String> values = new HashMap<>();
    final Map<String, String> infos = new HashMap<>();


    public Map<Date, String> getValues() {
        return values;
    }

    public Map<String, String> getInfos() {
        return infos;
    }
}
