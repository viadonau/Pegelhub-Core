package com.stm.pegelhub.connector.tstp.parsing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlGetDefinition {
    @XmlAttribute(name = "REIHENART")
    private String reihenart;
    @XmlAttribute(name = "TEXT")
    private String text;
    @XmlAttribute(name = "DEFART")
    private String defArt;
    @XmlAttribute(name = "EINHEIT")
    private String einheit;
    @XmlAttribute(name = "LEN")
    private String len;
    @XmlAttribute(name = "ANZ")
    private String anz;

    public String getReihenart() {
        return reihenart;
    }

    public void setReihenart(String reihenart) {
        this.reihenart = reihenart;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    public String getLen() {
        return len;
    }

    public void setLen(String len) {
        this.len = len;
    }

    public String getAnz() {
        return anz;
    }

    public void setAnz(String anz) {
        this.anz = anz;
    }

    public String getDefArt() {
        return defArt;
    }

    public void setDefArt(String defArt) {
        this.defArt = defArt;
    }
}
