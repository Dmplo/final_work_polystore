package dev.plotnikov.polystore.filters;

import dev.plotnikov.polystore.entities.Role;
import dev.plotnikov.polystore.entities.User;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CustomObject {

    private Map<String, String> tokens;
    private final UserDTO user;

    public CustomObject(User user) {
        this.user = new UserDTO(user);
        tokens = new HashMap<>();
    }
    public void add(String key, String value) {
        tokens.put(key, value);
    }
}

@Data
class UserDTO {
    Long id;
    String username;
    String lastname;
    String firstname;

    private List<Role> roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.lastname = user.getLastname();
        this.firstname = user.getFirstname();
        this.roles = user.getRoles();
    }
}