package com.stm.pegelhub.connector.tstp.xmlparsing;

public enum Method {
    READ("read"),
    WRITE("write");

    public final String name;

    Method(String name) {
        this.name = name;
    }

    public static Method valueOfName(String name) {
        for (Method type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
