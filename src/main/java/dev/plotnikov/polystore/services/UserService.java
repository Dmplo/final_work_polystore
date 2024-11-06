package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.DTOs.usersRoles.UsersRolesDTO;
import dev.plotnikov.polystore.entities.DTOs.usersRoles.UsersRolesDTOResultTransformer;
import dev.plotnikov.polystore.entities.User;
import dev.plotnikov.polystore.repositories.UserRepository;
import dev.plotnikov.polystore.util.EventType;
import dev.plotnikov.polystore.util.ObjectType;
import dev.plotnikov.polystore.util.Views;
import dev.plotnikov.polystore.util.WsSender;
import jakarta.persistence.EntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiConsumer;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final BiConsumer<EventType, User> wsSender;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, EntityManager entityManager, WsSender wsSender) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
        this.wsSender = wsSender.getSender(ObjectType.USER, Views.Full.class);
    }

    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        final User created = repository.save(user);
        wsSender.accept(EventType.CREATE, created);
        return created;
    }

    public User update(User user) {
        User userFromDb = getUserByUsername(user.getUsername());
        String ignoreFields = "id,password,createdAt,updatedAt,username";
        if (user.getRoles().isEmpty()) {
            ignoreFields += ",roles";
        }
        String[] ignore = ignoreFields.split(",");
        BeanUtils.copyProperties(user, userFromDb, ignore);
        User updated = repository.save(userFromDb);
        wsSender.accept(EventType.UPDATE, updated);
        return updated;
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public Boolean checkUsername(String username) {
        return repository.findByUsernameIgnoreCase(username).isPresent();
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь " + username + " не найден"));
    }

    public List<UsersRolesDTO> findUsersRoles() {
        return entityManager.createNativeQuery(getStringQuery()).unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new UsersRolesDTOResultTransformer())
                .getResultList();
    }

    private static String getStringQuery() {
        return """
                select
                        u.id as u_id,
                        u.username as u_user_name,
                        u.lastname as u_last_name,
                        u.firstname as u_first_name,
                        u.birth_date as u_birth_date,
                        u.phone as u_phone,
                        u.gender as u_gender,
                        r.id as r_id,
                        r.name as r_name
                        from users u
                        left join users_roles ur on ur.user_id = u.id
                        left join roles r on r.id = ur.roles_id
                        """;
    }
}
