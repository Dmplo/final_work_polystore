package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.DTOs.flangesByTypes.FlangesByTypesDTOResultTransformer;
import dev.plotnikov.polystore.entities.DTOs.flangesByTypes.TypesFlangesDTO;
import dev.plotnikov.polystore.entities.Flange;
import dev.plotnikov.polystore.repositories.FlangeRepository;
import dev.plotnikov.polystore.util.EventType;
import dev.plotnikov.polystore.util.ObjectType;
import dev.plotnikov.polystore.util.Views;
import dev.plotnikov.polystore.util.WsSender;
import jakarta.persistence.EntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class FlangeService {

    private final EntityManager entityManager;
    private final BiConsumer<EventType, Flange> wsSender;
    private final FlangeRepository repository;

    public FlangeService(EntityManager entityManager, WsSender wsSender, FlangeRepository repository) {
        this.entityManager = entityManager;
        this.wsSender = wsSender.getSender(ObjectType.FLANGE, Views.Full.class);
        this.repository = repository;
    }

    public Optional<Flange> findById(Long id) {
        return repository.findById(id);
    }

    public List<Flange> findAll() {
        return repository.findAll();
    }

    public Flange create(Flange flange) {
        final Flange created = repository.save(flange);
        wsSender.accept(EventType.CREATE, created);
        return created;
    }

    public Flange update(Flange flange) {
        Optional<Flange> flangeFromDbOptional = repository.findById(flange.getId());
        if (flangeFromDbOptional.isEmpty()) {
            throw new NoSuchElementException("Фланца с id = " + flange.getId() + " не существует!");
        }
        Flange flangeFromDb = flangeFromDbOptional.get();
        BeanUtils.copyProperties(flange, flangeFromDb, "id", "createdAt", "updatedAt");
        Flange updated = repository.save(flangeFromDb);
        wsSender.accept(EventType.UPDATE, updated);
        return updated;
    }

    public void delete(Flange flange) {
        repository.delete(flange);
        wsSender.accept(EventType.REMOVE, flange);
    }

    public List<TypesFlangesDTO> getFlangesByTypes() {
        return entityManager.createNativeQuery("""
                        select
                        f.type_id as t_id,
                        f.name as f_name,
                        f.id as f_id
                        from flanges f
                        """).unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new FlangesByTypesDTOResultTransformer())
                .getResultList();
    }
}