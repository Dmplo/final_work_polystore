package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {}
