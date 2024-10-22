package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.Pam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PamRepository extends JpaRepository<Pam, Long> {}
