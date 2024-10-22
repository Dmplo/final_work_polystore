package dev.plotnikov.polystore.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import dev.plotnikov.polystore.entities.Type;
import dev.plotnikov.polystore.services.TypeService;
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
@RequestMapping("api/types")
@Tag(name = "Type", description = "API для работы с видами продуктов")
@AllArgsConstructor
public class TypeController {

    private final TypeService service;

    @Operation(
            summary = "Get types",
            description = "Получить все типы продуктов",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Type.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping
    @JsonView(Views.MinParams.class)
    public ResponseEntity<List<Type>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Get type",
            description = "Получить тип продукта по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Type.class))),
                    @ApiResponse(description = "Тип не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/{id}")
    @JsonView(Views.Full.class)
    public ResponseEntity<Type> getOne(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Operation(
            summary = "Create type",
            description = "Создать тип продукта",
            responses = {
                    @ApiResponse(description = "Успешно создан", responseCode = "201", content = @Content(schema = @Schema(implementation = Type.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/save")
    public ResponseEntity<Type> create(@RequestBody Type address) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(address));
    }

    @Operation(
            summary = "Update type",
            description = "Обновить данные у типа",
            responses = {
                    @ApiResponse(description = "Успешно обновлен", responseCode = "200", content = @Content(schema = @Schema(implementation = Type.class))),
                    @ApiResponse(description = "Тип не найдена", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PutMapping("/update")
    public ResponseEntity<Type> update(@RequestBody Type type) {
        return ResponseEntity.ok(service.update(type));
    }

    @Operation(
            summary = "Delete type",
            description = "Удалить мощность по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешно удалена", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Тип не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Type type) {
        service.delete(type);
        return ResponseEntity.noContent().build();
    }
}
