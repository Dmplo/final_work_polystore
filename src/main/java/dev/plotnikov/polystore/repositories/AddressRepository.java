package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.Address;
import dev.plotnikov.polystore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByNameIgnoreCase(String name);

}
