package com.stm.pegelhub.connector.tstp.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter @Setter
@XmlRootElement(name = "TSR")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlTsResponse {
    @XmlAttribute(name = "RELEASE")
    private String release;
    @XmlValue
    private String message;
}
