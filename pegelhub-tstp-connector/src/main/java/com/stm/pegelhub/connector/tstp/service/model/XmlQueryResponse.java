package com.stm.pegelhub.connector.tstp.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter @Setter
@XmlRootElement(name = "TSQ")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlQueryResponse {
    @XmlElement(name = "TSATTR")
    private List<XmlQueryTsAttribut> def;
}
