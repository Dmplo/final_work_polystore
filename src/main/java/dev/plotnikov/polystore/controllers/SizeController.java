package dev.plotnikov.polystore.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import dev.plotnikov.polystore.entities.DTOs.sizesByTypes.TypesSizesDTO;
import dev.plotnikov.polystore.entities.Size;
import dev.plotnikov.polystore.services.SizeService;
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
@RequestMapping("api/sizes")
@Tag(name = "Size", description = "API для работы с габаритными размерами продуктов")
@AllArgsConstructor
public class SizeController {

    private final SizeService service;

    @Operation(
            summary = "Get sizes",
            description = "Получить все размеры",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Size.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping
    @JsonView(Views.MinParams.class)
    public ResponseEntity<List<Size>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Get sizes by product type",
            description = "Получить все размеры по типу применяемого продукта",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = TypesSizesDTO.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/types")
    public ResponseEntity<List<TypesSizesDTO>> getAllByTypes() {
        return ResponseEntity.ok(service.getSizesByTypes());
    }

    @Operation(
            summary = "Get size",
            description = "Получить размер по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Size.class))),
                    @ApiResponse(description = "Размер не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/{id}")
    @JsonView(Views.Full.class)
    public ResponseEntity<Size> getOne(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create size",
            description = "Создать размер",
            responses = {
                    @ApiResponse(description = "Успешно создан", responseCode = "201", content = @Content(schema = @Schema(implementation = Size.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/save")
    public ResponseEntity<Size> create(@RequestBody Size size) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(size));
    }

    @Operation(
            summary = "Update size",
            description = "Обновить данные размера",
            responses = {
                    @ApiResponse(description = "Успешно обновлен", responseCode = "200", content = @Content(schema = @Schema(implementation = Size.class))),
                    @ApiResponse(description = "Размер не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PutMapping("/update")
    public ResponseEntity<Size> update(@RequestBody Size size) {
        return ResponseEntity.ok(service.update(size));
    }

    @Operation(
            summary = "Delete size",
            description = "Удалить размер по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешно удален", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Фланец не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Size size) {
        service.delete(size);
        return ResponseEntity.noContent().build();
    }
}
