package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {}
