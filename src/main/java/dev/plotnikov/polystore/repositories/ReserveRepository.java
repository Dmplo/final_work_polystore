package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.DTOs.ReserveDTO;
import dev.plotnikov.polystore.entities.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    @Query(value = """
            select new dev.plotnikov.polystore.entities.DTOs.ReserveDTO(r.id, r.qty, r.comment, r.updatedAt, ap.id, ap.address.id, ap.space, concat(u.firstname, ' ', u.lastname) )
            from Reserve r
            join r.addressProduct ap
            join r.user u
            where r.id = :id
            """)
    Optional<ReserveDTO> findWithAddressProduct(long id);
}