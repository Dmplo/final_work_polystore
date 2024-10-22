package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.Flange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlangeRepository extends JpaRepository<Flange, Long> {}

