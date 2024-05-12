package com.stm.pegelhub.connector.tstp.parsing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TSD")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlGetResponse {
    @XmlElement(name = "DEF")
    private XmlGetDefinition def;
    @XmlElement(name = "DATA")
    private String data;


    public XmlGetDefinition getDef() {
        return def;
    }

    public void setDef(XmlGetDefinition def) {
        this.def = def;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
