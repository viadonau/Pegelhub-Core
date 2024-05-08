package com.stm.pegelhub.connector.tstp.xmlparsing;

public enum ConnectorMode {
    READ("read"),
    WRITE("write");

    public final String name;

    ConnectorMode(String name) {
        this.name = name;
    }

    public static ConnectorMode valueOfName(String name) {
        for (ConnectorMode type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
