package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.Role;
import dev.plotnikov.polystore.entities.RoleToUserForm;
import dev.plotnikov.polystore.entities.User;
import dev.plotnikov.polystore.repositories.RoleRepository;
import dev.plotnikov.polystore.util.EventType;
import dev.plotnikov.polystore.util.ObjectType;
import dev.plotnikov.polystore.util.Views;
import dev.plotnikov.polystore.util.WsSender;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiConsumer;

@Service
public class RoleService {

    private final UserService service;
    private final RoleRepository repository;
    private final BiConsumer<EventType, RoleToUserForm> wsSender;

    public RoleService(UserService service, RoleRepository repository, WsSender wsSender) {
        this.service = service;
        this.repository = repository;
        this.wsSender = wsSender.getSender(ObjectType.ROLE, null);
    }

    public Role saveRole(Role role) {
        return repository.save(role);
    }

    private void sendWs(String username, String roleName, EventType eventType) {
        RoleToUserForm form = new RoleToUserForm();
        form.setRoleName(roleName);
        form.setUsername(username);
        wsSender.accept(eventType, form);
    }

    @Transactional
    public void addRoleToUser(String username, String roleName) {
        User user = service.getUserByUsername(username);
        Role role = getRoleByName(roleName);
        user.getRoles().add(role);
        sendWs(username, roleName, EventType.ADD);
    }

    public Role getRoleByName(String roleName) throws UsernameNotFoundException {
        return repository.findByName(roleName)
                .orElseThrow(() -> new UsernameNotFoundException("Роль " + roleName + " не найдена!"));
    }

    @Transactional
    public void removeRoleToUser(String username, String roleName) {
        String myName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (myName.equals(username) && roleName.equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException("Пользователь не может удалять у себя роль админа!");
        } else {
            User user = service.getUserByUsername(username);
            user.setRoles(user.getRoles().stream().filter(it -> !it.getName().equals(roleName)).toList());
        }
        sendWs(username, roleName, EventType.REMOVE);
    }

    public List<Role> findAll() {
       return repository.findAll();
    }
}
