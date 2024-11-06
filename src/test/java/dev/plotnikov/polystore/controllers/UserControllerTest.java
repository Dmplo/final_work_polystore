package dev.plotnikov.polystore.controllers;


import dev.plotnikov.polystore.entities.Gender;
import dev.plotnikov.polystore.entities.IsCheckName;
import dev.plotnikov.polystore.entities.Role;
import dev.plotnikov.polystore.entities.User;
import dev.plotnikov.polystore.repositories.RoleRepository;
import dev.plotnikov.polystore.repositories.UserRepository;
import dev.plotnikov.polystore.services.AuthService;
import dev.plotnikov.polystore.services.RoleService;
import dev.plotnikov.polystore.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    private List<User> users = new ArrayList<>();

    private List<Role> roles = new ArrayList<>();

    private String token;

    @LocalServerPort
    private int port;
    private RestClient restClient;

    private LocalDate generateRandomDate() {
        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2010, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    private void createUsers() {
        createRoles();
        userService.create(new User(null, "John", "Travolta", "john", "1234", generateRandomDate(), String.valueOf(ThreadLocalRandom.current().nextInt(1111111, 9999999)), Gender.MALE, List.of(getRandom(roles)), null, null));
        userService.create(new User(null, "Will", "Smith", "will", "1234", generateRandomDate(), String.valueOf(ThreadLocalRandom.current().nextInt(1111111, 9999999)), Gender.MALE, List.of(getRandom(roles)), null, null));
        userService.create(new User(null, "Jim", "Carry", "jim", "1234", generateRandomDate(), String.valueOf(ThreadLocalRandom.current().nextInt(1111111, 9999999)), Gender.MALE, List.of(getRandom(roles)), null, null));
        userService.create(new User(null, "Arnold", "Schwarzenegger", "arnold", "1234", generateRandomDate(), String.valueOf(ThreadLocalRandom.current().nextInt(1111111, 9999999)), Gender.MALE, List.of(getRandom(roles)), null, null));
        users = userService.getUsers();
    }

    private void createRoles() {
        roleService.saveRole(new Role(null, "ROLE_USER"));
        roleService.saveRole(new Role(null, "ROLE_ADMIN"));
        roles = roleService.findAll();
    }

    public static <T> T getRandom(List<? extends T> items) {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, items.size());
        return items.get(randomIndex);
    }

    private Role findRole(String name) {
        return roles.stream().filter(role -> role.getName().equals(name))
                .findFirst().orElse(null);
    }

    @BeforeEach
    void beforeEach() {
        restClient = RestClient.create("http://localhost:" + port);
        final Instant accessExpirationInstant = LocalDateTime.now().plusDays(2).atZone(ZoneId.systemDefault()).toInstant();
        token = authService.createAccessTokenWithUser(accessExpirationInstant, "alex", List.of("ROLE_USER", "ROLE_ADMIN"), "http://localhost:8080/api/login");
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        users.clear();
    }

    @Test
    public void testGetAll() {

        webTestClient
                .get().uri("/api/users")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testUpdate() {
        createUsers();
        User toUpdate = getRandom(users);
        toUpdate.setFirstname(toUpdate.getFirstname() + "change");
        toUpdate.setLastname(toUpdate.getLastname() + "change");
        List<Role> rolesUpd = toUpdate.getRoles();
        rolesUpd.add(toUpdate.getRoles().contains(findRole("ROLE_ADMIN")) ? findRole("ROLE_USER") : findRole("ROLE_ADMIN"));
        toUpdate.setRoles(rolesUpd);

        ResponseEntity<User> response =
                restClient.put()
                        .uri("/api/users/update").headers(http -> http.setBearerAuth(token))
                        .body(toUpdate)
                        .retrieve()
                        .toEntity(User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseBody = response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.getId());
        assertEquals(responseBody.getFirstname(), toUpdate.getFirstname());
        assertEquals(responseBody.getRoles().size(), toUpdate.getRoles().size());
    }

    @Test
    void testIsExistsUsername() {
        createUsers();
        User user = getRandom(users);
        ResponseEntity<IsCheckName> actual =
                restClient.get()
                        .uri("/api/users/checkname/" + user.getUsername())
                        .headers(http -> http.setBearerAuth(token))
                        .retrieve()
                        .toEntity(IsCheckName.class);
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        IsCheckName responseBody = actual.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.getIsExists());
    }

    @Test
    void testCreate() {
        createRoles();
        Role role = getRandom(roles);
        User toCreate = new User();
        toCreate.setFirstname("Alex");
        toCreate.setLastname("Anderson");
        toCreate.setUsername("alex");
        toCreate.setPhone("123456");
        toCreate.setPassword("123456");
        toCreate.setRoles(List.of(role));
        toCreate.setGender(Gender.MALE);

        ResponseEntity<User> response =
                restClient.post()
                        .uri("/api/users/save").headers(http -> http.setBearerAuth(token))
                        .body(toCreate)
                        .retrieve()
                        .toEntity(User.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User responseBody = response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.getId());
        assertEquals(responseBody.getFirstname(), toCreate.getFirstname());
        assertTrue(responseBody.getRoles().contains(role));
        assertTrue(userRepository.existsById(responseBody.getId()));
    }
}