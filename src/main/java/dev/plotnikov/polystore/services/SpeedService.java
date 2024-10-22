package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.Speed;
import dev.plotnikov.polystore.repositories.SpeedRepository;
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
public class SpeedService {

    private final SpeedRepository repository;
    private final BiConsumer<EventType, Speed> wsSender;

    public SpeedService(SpeedRepository repository, WsSender wsSender) {
        this.repository = repository;
        this.wsSender = wsSender.getSender(ObjectType.SPEED, Views.Full.class);
    }

    public Optional<Speed> findById(Long id) {
        return repository.findById(id);
    }

    public List<Speed> findAll() {
        return repository.findAll();
    }

    public Speed create(Speed speed) {
        final Speed created = repository.save(speed);
        wsSender.accept(EventType.CREATE, created);
        return created;
    }

    public Speed update(Speed speed) {
        Optional<Speed> speedFromDbOptional = repository.findById(speed.getId());
        if (speedFromDbOptional.isEmpty()) {
            throw new NoSuchElementException("Скорости с id = " + speed.getId() + " не существует!");
        }
        Speed speedFromDb = speedFromDbOptional.get();
        BeanUtils.copyProperties(speed, speedFromDb, "id", "createdAt", "updatedAt");
        Speed updated = repository.save(speedFromDb);
        wsSender.accept(EventType.UPDATE, updated);
        return updated;
    }

    public void delete(Speed speed) {
        repository.delete(speed);
        wsSender.accept(EventType.REMOVE, speed);
    }

}