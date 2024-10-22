package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.Speed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeedRepository extends JpaRepository<Speed, Long> {}
