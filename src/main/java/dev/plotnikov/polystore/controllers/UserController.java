package dev.plotnikov.polystore.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import dev.plotnikov.polystore.entities.DTOs.usersRoles.UsersRolesDTO;
import dev.plotnikov.polystore.entities.IsCheckName;
import dev.plotnikov.polystore.entities.User;
import dev.plotnikov.polystore.services.UserService;
import dev.plotnikov.polystore.util.Views;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "User", description = "API для работы с пользователями")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(
            summary = "Get user profile",
            description = "Получить профиль пользователя",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(description = "Пользователь не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/profile")
    @JsonView(Views.MinParams.class)
    public ResponseEntity<User> getOne() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(service.getUserByUsername(name));
    }

    @Operation(
            summary = "Get users",
            description = "Получить всех пользователей",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = UsersRolesDTO.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/users")
    public ResponseEntity<List<UsersRolesDTO>> getAll() {
        return ResponseEntity.ok(service.findUsersRoles());
    }

    @Operation(
            summary = "Create user",
            description = "Создать пользователя",
            responses = {
                    @ApiResponse(description = "Успешно создан", responseCode = "201", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/users/save")
    @JsonView(Views.Full.class)
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user));
    }

    @Operation(
            summary = "Check user login",
            description = "Проверить что логин не занят",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = IsCheckName.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/users/checkname/{username}")
    public ResponseEntity<IsCheckName> isExistsUsername(@PathVariable String username) {
        IsCheckName isCheckName = new IsCheckName();
        isCheckName.setIsExists(service.checkUsername(username));
        return ResponseEntity.ok(isCheckName);
    }

    @Operation(
            summary = "Update user",
            description = "Обновить пользователя",
            responses = {
                    @ApiResponse(description = "Успешно обновлен", responseCode = "200", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(description = "Пользователь не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PutMapping("/users/update")
    @JsonView(Views.Full.class)
    public ResponseEntity<User> update(@RequestBody User user) {
        return ResponseEntity.ok(service.update(user));
    }
}
