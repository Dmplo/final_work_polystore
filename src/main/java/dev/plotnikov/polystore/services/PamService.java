package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.Pam;
import dev.plotnikov.polystore.repositories.PamRepository;
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
public class PamService {

    private final PamRepository repository;
    private final BiConsumer<EventType, Pam> wsSender;

    public PamService(PamRepository repository, WsSender wsSender) {
        this.repository = repository;
        this.wsSender = wsSender.getSender(ObjectType.PAM, Views.Full.class);
    }

    public Optional<Pam> findById(Long id) {
        return repository.findById(id);
    }

    public List<Pam> findAll() {
        return repository.findAll();
    }

    public Pam update(Pam pam) {
        Optional<Pam> pamFromDbOptional = findById(pam.getId());
        if (pamFromDbOptional.isEmpty()) {
            throw new NoSuchElementException("Pam с id = " + pam.getId() + " не существует!");
        }
        Pam pamFromDb = pamFromDbOptional.get();
        BeanUtils.copyProperties(pam, pamFromDb, "id", "createdAt", "updatedAt");
        Pam updated = repository.save(pamFromDb);
        wsSender.accept(EventType.UPDATE, updated);
        return updated;
    }

    public Pam create(Pam pam) {
        final Pam created = repository.save(pam);
        wsSender.accept(EventType.CREATE, created);
        return created;
    }

    public void delete(Pam pam) {
        repository.delete(pam);
        wsSender.accept(EventType.REMOVE, pam);
    }

}
