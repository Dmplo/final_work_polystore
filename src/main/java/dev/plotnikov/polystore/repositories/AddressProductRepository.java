package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.AddressProduct;
import dev.plotnikov.polystore.entities.DTOs.AddressProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressProductRepository extends JpaRepository<AddressProduct, Long> {

    @Query(value = "select distinct new dev.plotnikov.polystore.entities.DTOs.AddressProductDTO(a.id, a.name, a.capacity, size(p))"
                   + " from Address a left join a.products p ORDER BY a.name, a.id")
    Page<AddressProductDTO> findAllCustom(Pageable pageable);

    @Query(value = "from AddressProduct ap where ap.address.id = :addressId and ap.product.id =:productId and ap.space = :space")
    Optional<AddressProduct> findByParams(Long addressId, Long productId, Integer space);

    @Query(value = "select DISTINCT new dev.plotnikov.polystore.entities.DTOs.AddressProductDTO(a.id, a.name, a.capacity, size(p))"
                   + " from Address a left join a.products p where a.id = :id")
    Optional<AddressProductDTO> findByIdCustom(Long id);
}


