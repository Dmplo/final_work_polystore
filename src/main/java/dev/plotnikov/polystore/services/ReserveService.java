package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.AddressProduct;
import dev.plotnikov.polystore.entities.DTOs.CalcMainDTO.CalcMainDTO;
import dev.plotnikov.polystore.entities.DTOs.ReserveDTO;
import dev.plotnikov.polystore.entities.Reserve;
import dev.plotnikov.polystore.entities.User;
import dev.plotnikov.polystore.repositories.AddressProductRepository;
import dev.plotnikov.polystore.repositories.ReserveRepository;
import dev.plotnikov.polystore.util.EventType;
import dev.plotnikov.polystore.util.ObjectType;
import dev.plotnikov.polystore.util.WsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class ReserveService {

    private final ReserveRepository repository;
    private final AddressProductRepository repositoryAP;
    private final UserService userService;
    private final AddressProductService service;
    private final BiConsumer<EventType, ReserveDTO> wsSenderDTO;

    @Autowired
    public ReserveService(ReserveRepository repository, AddressProductRepository repositoryAP, UserService userService, AddressProductService service, WsSender wsSender) {
        this.repository = repository;
        this.repositoryAP = repositoryAP;
        this.userService = userService;
        this.service = service;
        this.wsSenderDTO = wsSender.getSender(ObjectType.RESERVE, null);
    }

    public Optional<Reserve> findById(Long id) {
        return repository.findById(id);
    }

    public ReserveDTO findReserve(Long id) {
        return repository.findWithAddressProduct(id)
                .orElseThrow(() -> new NoSuchElementException("Резерва с id = " + id + " не существует!"));
    }

    public List<Reserve> findAll() {
        return repository.findAll();
    }

    public ReserveDTO create(Reserve reserve) {
        List<CalcMainDTO> qtyList = service.calcQtyProductsByAddressId(reserve.getAddressProduct().getId());
        if (!qtyList.isEmpty()) {
            CalcMainDTO result = qtyList.get(0);
            int total = result.getQty() - (result.getAllReserveQty() + reserve.getQty());
            if (total < 0) {
                throw new IllegalArgumentException("Недостаточно свободных остатков для резервирования");
            }
        }
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(name);
        reserve.setUser(user);
        final Reserve created = repository.save(reserve);
        ReserveDTO reserveDTO = findReserve(created.getId());
        wsSenderDTO.accept(EventType.CREATE, reserveDTO);
        return reserveDTO;
    }

    public void writeOff(Long id) {
        ReserveDTO reserveDTO = findReserve(id);
        repository
                .findById(id)
                .ifPresentOrElse((result) -> {
                    AddressProduct addressProduct = result.getAddressProduct();
                    int total = addressProduct.getQty() - result.getQty();
                    if (total > 0) {
                        addressProduct.setQty(total);
                        repositoryAP.save(addressProduct);
                    } else {
                        repositoryAP.delete(addressProduct);
                    }
                    repository.delete(result);
                }, () -> {
                    throw new NoSuchElementException("Резерва с id = " + id + " не существует!");
                });
        wsSenderDTO.accept(EventType.WRITEOFF, reserveDTO);
    }

    public void delete(Long id) {
        ReserveDTO reserveDTO = findReserve(id);
        repository.deleteById(id);
        wsSenderDTO.accept(EventType.REMOVE, reserveDTO);
    }

}