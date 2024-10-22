package dev.plotnikov.polystore.controllers;

import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import dev.plotnikov.polystore.entities.Address;
import dev.plotnikov.polystore.entities.Role;
import dev.plotnikov.polystore.entities.RoleToUserForm;
import dev.plotnikov.polystore.services.RoleService;
import dev.plotnikov.polystore.util.Views;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
@Tag(name = "Role", description = "API для работы с ролями пользователя")
public class RoleController {

    private final RoleService service;

    @Operation(
            summary = "Get roles",
            description = "Получить все роли",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Role.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping
    public ResponseEntity<List<Role>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Create role",
            description = "Создать роль",
            responses = {
                    @ApiResponse(description = "Успешно создана", responseCode = "201", content = @Content(schema = @Schema(implementation = Role.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveRole(role));
    }

    @Operation(
            summary = "Add role to user",
            description = "Добавить роль пользователю",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Пользователь не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/add-to-user")
    public ResponseEntity<Void> addRoleToUser(@RequestBody RoleToUserForm form) {
        service.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remove role to user",
            description = "Удалить роль у пользователя",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Пользователь не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PutMapping("/remove-to-user")
    public ResponseEntity<Void> removeRoleToUser(@RequestBody RoleToUserForm form) {
        service.removeRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.noContent().build();
    }

}