package com.stm.pegelhub.connector.tstp.parsing.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdapterCDATA extends XmlAdapter<String, String> {
    @Override
    public String marshal(String arg0) {
        return "<![CDATA[" + arg0 + "]]>";
    }

    @Override
    public String unmarshal(String arg0) {
        String data = arg0.split("\\[")[2];
        // -3 to remove ]] at the end
        return data.substring(0, data.length()-3);
    }
}
