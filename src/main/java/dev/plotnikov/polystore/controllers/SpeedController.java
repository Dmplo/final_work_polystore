package dev.plotnikov.polystore.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import dev.plotnikov.polystore.entities.Speed;
import dev.plotnikov.polystore.services.SpeedService;
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
@RequestMapping("api/speeds")
@Tag(name = "Speed", description = "API для работы со скоростями электродвигателей")
@AllArgsConstructor
public class SpeedController {

    private final SpeedService service;

    @Operation(
            summary = "Get speeds",
            description = "Получить все обороты",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Speed.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping
    @JsonView(Views.MinParams.class)
    public ResponseEntity<List<Speed>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Get speed",
            description = "Получить значение оборотов по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Speed.class))),
                    @ApiResponse(description = "Значение оборотов не найдено", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/{id}")
    @JsonView(Views.Full.class)
    public ResponseEntity<Speed> getOne(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Operation(
            summary = "Create speed",
            description = "Создать значение оборотов",
            responses = {
                    @ApiResponse(description = "Успешно создан", responseCode = "201", content = @Content(schema = @Schema(implementation = Speed.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/save")
    public ResponseEntity<Speed> create(@RequestBody Speed speed) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(speed));
    }

    @Operation(
            summary = "Update speed",
            description = "Обновить значение оборотов",
            responses = {
                    @ApiResponse(description = "Успешно обновлен", responseCode = "200", content = @Content(schema = @Schema(implementation = Speed.class))),
                    @ApiResponse(description = "Значение оборотов не найдено", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PutMapping("/update")
    public ResponseEntity<Speed> update(@RequestBody Speed speed) {
        return ResponseEntity.ok(service.update(speed));
    }

    @Operation(
            summary = "Delete speed",
            description = "Удалить значение оборотов по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешно удалена", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Мощность не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Speed speed) {
        service.delete(speed);
        return ResponseEntity.noContent().build();
    }
}
