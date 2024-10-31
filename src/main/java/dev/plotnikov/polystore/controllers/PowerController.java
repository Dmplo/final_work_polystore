package dev.plotnikov.polystore.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import dev.plotnikov.polystore.entities.Power;
import dev.plotnikov.polystore.services.PowerService;
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
@RequestMapping("api/powers")
@Tag(name = "Power", description = "API для работы с мощностями")
@AllArgsConstructor
public class PowerController {

    private final PowerService service;

    @Operation(
            summary = "Get powers",
            description = "Получить все мощности",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Power.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping
    @JsonView(Views.MinParams.class)
    public ResponseEntity<List<Power>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Get power",
            description = "Получить мощность по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Power.class))),
                    @ApiResponse(description = "Мощность не найдена", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/{id}")
    @JsonView(Views.Full.class)
    public ResponseEntity<Power> getOne(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create power",
            description = "Создать мощность",
            responses = {
                    @ApiResponse(description = "Успешно создан", responseCode = "201", content = @Content(schema = @Schema(implementation = Power.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/save")
    public ResponseEntity<Power> create(@RequestBody Power power) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(power));
    }

    @Operation(
            summary = "Update power",
            description = "Обновить данные мощности",
            responses = {
                    @ApiResponse(description = "Успешно обновлен", responseCode = "200", content = @Content(schema = @Schema(implementation = Power.class))),
                    @ApiResponse(description = "Мощность не найдена", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PutMapping("/update")
    public ResponseEntity<Power> update(@RequestBody Power power) {
        return ResponseEntity.ok(service.update(power));
    }

    @Operation(
            summary = "Delete power",
            description = "Удалить мощность по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешно удалена", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Мощность не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Power power) {
        service.delete(power);
        return ResponseEntity.noContent().build();
    }
}
