package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.DTOs.sizesByTypes.SizesByTypesDTOResultTransformer;
import dev.plotnikov.polystore.entities.DTOs.sizesByTypes.TypesSizesDTO;
import dev.plotnikov.polystore.entities.Size;
import dev.plotnikov.polystore.repositories.SizeRepository;
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
public class SizeService {

    private final EntityManager entityManager;
    private final SizeRepository repository;
    private final BiConsumer<EventType, Size> wsSender;

    public SizeService(EntityManager entityManager, SizeRepository repository, WsSender wsSender) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.wsSender = wsSender.getSender(ObjectType.SIZE, Views.Full.class);
    }

    public Optional<Size> findById(Long id) {
        return repository.findById(id);
    }

    public List<Size> findAll() {
        return repository.findAll();
    }

    public Size create(Size size) {
        final Size created = repository.save(size);
        wsSender.accept(EventType.CREATE, created);
        return created;
    }

    public Size update(Size size) {
        Optional<Size> flangeFromDbOptional = repository.findById(size.getId());
        if (flangeFromDbOptional.isEmpty()) {
            throw new NoSuchElementException("Размера с id = " + size.getId() + " не существует!");
        }
        Size flangeFromDb = flangeFromDbOptional.get();
        BeanUtils.copyProperties(size, flangeFromDb, "id", "createdAt", "updatedAt");
        Size updated = repository.save(flangeFromDb);
        wsSender.accept(EventType.UPDATE, updated);
        return updated;
    }

    public void delete(Size size) {
        repository.delete(size);
        wsSender.accept(EventType.REMOVE, size);
    }

    public List<TypesSizesDTO> getSizesByTypes() {
        return entityManager.createNativeQuery("""
                        select
                        st.type_id as t_id,
                        s.name as s_name,
                        s.id as s_id
                        from sizes_types st
                        left join sizes s on st.size_id=s.id
                        """).unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new SizesByTypesDTOResultTransformer())
                .getResultList();
    }

}
