package com.stm.pegelhub.connector.tstp.parsing.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Getter @Setter
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
}
