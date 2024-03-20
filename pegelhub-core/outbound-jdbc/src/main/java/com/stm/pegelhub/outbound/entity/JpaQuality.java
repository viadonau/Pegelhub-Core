package com.stm.pegelhub.outbound.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Data
@Table(name = "Quality")
public class JpaQuality extends IdentifiableEntity{
    @Column(nullable = false)
    private Integer qualityCode;
    @Column(nullable = false)
    private String plaintext;
}
