package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select id from Product where id = :id")
    Optional<Product> findProductCustom(Long id);

    @Query("select count(p) from Product p where p.type.id = :id")
    Long findProductsCount(Long id);
}
