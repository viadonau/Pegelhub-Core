package com.stm.pegelhub.connector.tstp.parsing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@XmlRootElement(name = "TSD")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlTsData {
    @XmlElement(name = "DEF")
    private XmlTsDefinition def;
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    @XmlElement(name = "DATA")
    private String data;
}
