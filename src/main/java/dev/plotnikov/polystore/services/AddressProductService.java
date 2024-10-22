package dev.plotnikov.polystore.services;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.entities.AddressProduct;
import dev.plotnikov.polystore.entities.DTOs.AddressProductDTO;
import dev.plotnikov.polystore.entities.DTOs.AddressProductDTOSec;
import dev.plotnikov.polystore.entities.DTOs.AddressProductPageDTO;
import dev.plotnikov.polystore.entities.DTOs.CalcMainDTO.CalcMainDTO;
import dev.plotnikov.polystore.entities.DTOs.CalcMainDTO.CalcQtyProductsDTOResultTransformer;
import dev.plotnikov.polystore.entities.User;
import dev.plotnikov.polystore.repositories.AddressProductRepository;
import dev.plotnikov.polystore.util.EventType;
import dev.plotnikov.polystore.util.ObjectType;
import dev.plotnikov.polystore.util.Views;
import dev.plotnikov.polystore.util.WsSender;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class AddressProductService {

    private final AddressProductRepository repository;
    private final EntityManager entityManager;

    private final BiConsumer<EventType, AddressProductDTOSec> wsSender;
    private final BiConsumer<EventType, Map<String, Long>> wsSenderSec;

    public AddressProductService(AddressProductRepository repository, EntityManager entityManager, WsSender wsSender) {
        this.repository = repository;
        this.entityManager = entityManager;
        this.wsSender = wsSender.getSender(ObjectType.ADDRESS_PRODUCT, null);
        this.wsSenderSec = wsSender.getSender(ObjectType.ADDRESS_PRODUCT, null);
    }

    public Optional<AddressProduct> findById(Long id) {
        return repository.findById(id);
    }

    public AddressProductPageDTO findAll(Pageable pageable) {
        Page<AddressProductDTO> page = repository.findAllCustom(pageable);
        return new AddressProductPageDTO(page.getContent(), pageable.getPageNumber(), page.getTotalPages());
    }

    public AddressProductDTO findByIdCustom(Long id) {
        return repository.findByIdCustom(id)
                .orElseThrow(() -> new NoSuchElementException("Адреса с id = " + id + " не существует!"));
    }

    public AddressProduct findOrCreate(AddressProduct addressProduct) {
        Optional<AddressProduct> addressProductOpt = findByParams(addressProduct.getAddress().getId(), addressProduct.getProduct().getId(), addressProduct.getSpace());
        if (addressProductOpt.isPresent()) {
            AddressProduct addressProductFromDB = addressProductOpt.get();
            addressProductFromDB.setQty(addressProductFromDB.getQty() + addressProduct.getQty());
            return repository.save(addressProductFromDB);
        }
        final AddressProduct created = repository.save(addressProduct);
        Map<String, Long> responceMap = Map.of(
                "addressId", created.getAddress().getId(),
                "productId", created.getProduct().getId(),
                "typeId", created.getProduct().getType().getId()
        );
        wsSenderSec.accept(EventType.CREATE, responceMap);
        return created;
    }

    public void updateOrDelete(AddressProduct addressProduct) {
        checkAddressProduct(addressProduct.getId(), addressProduct.getQty());
        AddressProductDTOSec addressProductDTOSec = new AddressProductDTOSec(addressProduct.getId(), addressProduct.getAddress().getName(), addressProduct.getProduct().getType().getId(), addressProduct.getProduct().getId(), addressProduct.getQty(), addressProduct.getSpace());
        findByIdSimple(addressProduct.getId())
                .ifPresentOrElse(
                        item -> {
                            int totalQty = item.getQty() - addressProduct.getQty();
                            if (totalQty > 0) {
                                item.setQty(totalQty);
                                repository.save(item);
                                wsSender.accept(EventType.UPDATE, addressProductDTOSec);
                            } else {
                                repository.delete(item);
                                wsSender.accept(EventType.REMOVE, addressProductDTOSec);
                            }
                        },
                        () -> {
                            throw new NoSuchElementException("Продукта по адресу хранения с id = " + addressProduct.getId() + " не существует!");
                        });
    }

    private void checkAddressProduct(long id, int qty) {
        List<CalcMainDTO> qtyList = calcQtyProductsByAddressIdSec(id);
        if (!qtyList.isEmpty()) {
            CalcMainDTO result = qtyList.get(0);
            int total = result.getQty() - (result.getAllReserveQty() + qty);
            if (total < 0) {
                throw new IllegalArgumentException("Недостаточно доступных остатков для списания продукта");
            }
        } else {
            throw new NoSuchElementException("Продукта по адресу хранения с id = " + id + " не существует!");
        }
    }

    @JsonView(Views.MinParams.class)
    private Optional<AddressProduct> findByIdSimple(Long id) {
        return repository.findById(id);
    }

    @JsonView(Views.MinParams.class)
    public Optional<AddressProduct> findByParams(Long addressId, Long productId, Integer space) {
        return repository.findByParams(addressId, productId, space);
    }

    public List<CalcMainDTO> calcQtyProductsByAddressId(Long id) {
        return entityManager.createNativeQuery("""
                            select
                            r.qty as r_qty,
                            r.id as r_id,
                            ap.id as ap_id,
                            ap.qty as ap_qty
                            from addresses_products ap
                            left join reserves r on r.address_id = ap.id
                            where r.address_id = ?1
                        """).setParameter(1, id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new CalcQtyProductsDTOResultTransformer())
                .getResultList();
    }

    public List<CalcMainDTO> calcQtyProductsByAddressIdSec(Long id) {
        return entityManager.createNativeQuery("""
                            select
                            r.qty as r_qty,
                            r.id as r_id,
                            ap.id as ap_id,
                            ap.qty as ap_qty
                            from addresses_products ap
                            left join reserves r on r.address_id = ap.id
                            where ap.id = ?1
                        """).setParameter(1, id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new CalcQtyProductsDTOResultTransformer())
                .getResultList();
    }
}