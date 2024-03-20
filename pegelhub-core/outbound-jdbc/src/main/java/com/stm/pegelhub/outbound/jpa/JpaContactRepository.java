package com.stm.pegelhub.outbound.jpa;

import com.stm.pegelhub.outbound.entity.JpaContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * JPA repository for {@code Contact}s.
 */

@Repository
public interface JpaContactRepository extends JpaRepository<JpaContact, UUID> {
}
