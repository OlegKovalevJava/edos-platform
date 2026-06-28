package com.logistic.edo.entrypoint.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentJpaRepository extends JpaRepository<DocumentEntity, UUID> {
}
