package com.stm.pegelhub.connector.tstp.parsing.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter @Setter
@XmlRootElement(name = "TSD")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlGetResponse {
    @XmlElement(name = "DEF")
    private XmlGetDefinition def;
    @XmlElement(name = "DATA")
    private String data;
}
