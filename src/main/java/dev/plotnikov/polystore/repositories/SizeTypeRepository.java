package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.SizeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeTypeRepository extends JpaRepository<SizeType, Long> {}


