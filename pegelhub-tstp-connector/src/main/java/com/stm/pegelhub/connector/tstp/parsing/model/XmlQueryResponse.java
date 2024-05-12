package com.stm.pegelhub.connector.tstp.parsing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "TSQ")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlQueryResponse {
    @XmlElement(name = "TSATTR")
    private List<XmlQueryTsAttribut> def;

    public List<XmlQueryTsAttribut> getDef() {
        return def;
    }

    public void setDef(List<XmlQueryTsAttribut> def) {
        this.def = def;
    }
}
