package com.stm.pegelhub.connector.tstp.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlTsDefinition {
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
