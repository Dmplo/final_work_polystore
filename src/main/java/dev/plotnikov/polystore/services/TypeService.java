package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.Type;
import dev.plotnikov.polystore.repositories.TypeRepository;
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
public class TypeService {

    private final TypeRepository repository;
    private final BiConsumer<EventType, Type> wsSender;

    public TypeService(TypeRepository repository, WsSender wsSender) {
        this.repository = repository;
        this.wsSender = wsSender.getSender(ObjectType.TYPE, Views.Full.class);
    }

    public Optional<Type> findById(Long id) {
        return repository.findById(id);
    }

    public List<Type> findAll() {
        return repository.findAll();
    }

    public Type create(Type type) {
        final Type created = repository.save(type);
        wsSender.accept(EventType.CREATE, created);
        return created;
    }

    public Type update(Type type) {
        Optional<Type> typeFromDbOptional = repository.findById(type.getId());
        if (typeFromDbOptional.isEmpty()) {
            throw new NoSuchElementException("Типа с id = " + type.getId() + " не существует!");
        }
        Type typeFromDb = typeFromDbOptional.get();
        BeanUtils.copyProperties(type, typeFromDb, "id", "createdAt", "updatedAt");
        Type updated = repository.save(typeFromDb);
        wsSender.accept(EventType.UPDATE, updated);
        return updated;
    }

    public void delete(Type type) {
        repository.delete(type);
        wsSender.accept(EventType.REMOVE, type);
    }

}
