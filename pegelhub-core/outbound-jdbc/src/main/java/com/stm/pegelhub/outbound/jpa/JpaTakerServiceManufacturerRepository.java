package com.stm.pegelhub.outbound.jpa;

import com.stm.pegelhub.outbound.entity.JpaTakerServiceManufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * JPA repository for {@code ServiceManufacturer}s.
 */

@Repository
public interface JpaTakerServiceManufacturerRepository extends JpaRepository<JpaTakerServiceManufacturer, UUID> {
}
