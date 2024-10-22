package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.Power;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerRepository extends JpaRepository<Power, Long> {}
