package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.Address;
import dev.plotnikov.polystore.entities.DTOs.AddressesByProductDTO.AddressesByProductDTO;
import dev.plotnikov.polystore.entities.DTOs.AddressesByProductDTO.AddressesByProductDTOResultTransformer;
import dev.plotnikov.polystore.repositories.AddressRepository;
import dev.plotnikov.polystore.repositories.ProductRepository;
import dev.plotnikov.polystore.util.EventType;
import dev.plotnikov.polystore.util.ObjectType;
import dev.plotnikov.polystore.util.Views;
import dev.plotnikov.polystore.util.WsSender;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class AddressService {

    private final EntityManager entityManager;
    private final BiConsumer<EventType, Address> wsSender;
    private final AddressRepository repository;
    private final ProductRepository productRepository;

    public AddressService(EntityManager entityManager, WsSender wsSender, AddressRepository repository, ProductRepository productRepository) {
        this.entityManager = entityManager;
        this.wsSender = wsSender.getSender(ObjectType.ADDRESS, Views.Full.class);;
        this.repository = repository;
        this.productRepository = productRepository;
    }

    public Optional<Address> findById(Long id) {
        return repository.findById(id);
    }

    public List<Address> findAll() {
        return repository.findAll();
    }

    public Address create(Address address) {
        final Address created = repository.save(address);
        wsSender.accept(EventType.CREATE, created);
        return created;
    }

    public Boolean checkAddressName(String name) {
        return repository.findByNameIgnoreCase(name).isPresent();
    }

    public List<AddressesByProductDTO> findAddressesByProductId(Long id) {
        if (productRepository.findProductCustom(id).isEmpty()) {
            throw new NoSuchElementException("Продукта с id = " + id + " не существует!");
        }
        return entityManager.createNativeQuery("""
                            select
                            p.id as p_id,
                            p.type as p_type,
                            p.type_id as p_type_id,
                            p.name as p_name,
                            p.ratio as p_ratio,
                            s.name as p_size,
                            pw.name as p_power,
                            sp.name as p_speed,
                            f.name as p_flange,
                            pam.name as p_pam,
                            r.id as r_id,
                            CONCAT (u.firstname, ' ', u.lastname) as u_name,
                            r.comment as r_comment,
                            r.qty as r_qty,
                            r.updated_at as r_updated_at,
                            a.name as a_name,
                            a.id as a_id,
                            ap.id as ap_id,
                            ap.space as ap_space,
                            ap.updated_at as ap_updated_at,
                            ap.qty as ap_qty,
                            regexp_replace(
                                regexp_replace(a.name, '(\\d+)', '000000\\1')
                                , '0*(\\d{4})', '\\1' ) reg
                            from addresses a
                            left join addresses_products ap on ap.address_id = a.id
                            left join products p on p.id = ap.product_id
                            left join reserves r on r.address_id = ap.id
                            left join sizes s on p.size_id = s.id
                            left join flanges f on p.flange_id = f.id
                            left join powers pw on p.power_id = pw.id
                            left join speeds sp on p.speed_id = sp.id
                            left join pams pam on p.pam_id = pam.id
                            left join users u on r.user_id = u.id
                            where p.id = ?1
                            order by reg, a.name, ap.space
                        """).setParameter(1, id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AddressesByProductDTOResultTransformer())
                .getResultList();
    }

}