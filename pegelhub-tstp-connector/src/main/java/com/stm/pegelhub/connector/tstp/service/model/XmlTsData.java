package com.stm.pegelhub.connector.tstp.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@XmlRootElement(name = "TSD")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlTsData {
    @XmlAttribute(name = "RELEASE")
    private String release;
    @XmlElement(name = "DEF")
    private XmlTsDefinition def;
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    @XmlElement(name = "DATA")
    private String data;
}
