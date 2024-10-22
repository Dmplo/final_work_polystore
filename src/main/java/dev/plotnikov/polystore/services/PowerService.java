package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.Power;
import dev.plotnikov.polystore.repositories.PowerRepository;
import dev.plotnikov.polystore.util.EventType;
import dev.plotnikov.polystore.util.ObjectType;
import dev.plotnikov.polystore.util.Views;
import dev.plotnikov.polystore.util.WsSender;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class PowerService {

    private final PowerRepository repository;
    private final BiConsumer<EventType, Power> wsSender;

    public PowerService(PowerRepository repository, WsSender wsSender) {
        this.repository = repository;
        this.wsSender = wsSender.getSender(ObjectType.POWER, Views.Full.class);
    }

    public Optional<Power> findById(Long id) {
        return repository.findById(id);
    }

    public List<Power> findAll() {
        return repository.findAll();
    }

    public Power update(Power power) {
        Optional<Power> powerFromDbOptional = findById(power.getId());
        if (powerFromDbOptional.isEmpty()) {
            throw new NoSuchElementException("Мощности с id = " + power.getId() + " не существует!");
        }
        Power powerFromDb = powerFromDbOptional.get();
        BeanUtils.copyProperties(power, powerFromDb, "id", "createdAt", "updatedAt");
        Power updated = repository.save(powerFromDb);
        wsSender.accept(EventType.UPDATE, updated);
        return updated;
    }

    public Power create(Power power) {
        final Power created = repository.save(power);
        wsSender.accept(EventType.CREATE, created);
        return created;
    }

    public void delete(Power power) {
        repository.delete(power);
        wsSender.accept(EventType.REMOVE, power);
    }

}