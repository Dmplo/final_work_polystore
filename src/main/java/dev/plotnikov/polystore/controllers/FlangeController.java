package dev.plotnikov.polystore.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import dev.plotnikov.polystore.entities.DTOs.flangesByTypes.TypesFlangesDTO;
import dev.plotnikov.polystore.entities.Flange;
import dev.plotnikov.polystore.services.FlangeService;
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
@RequestMapping("api/flanges")
@AllArgsConstructor
@Tag(name = "Flange", description = "API для работы с фланцами")
public class FlangeController {

    private final FlangeService service;

    @Operation(
            summary = "Get flanges",
            description = "Получить все фланцы",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Flange.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping
    @JsonView(Views.MinParams.class)
    public ResponseEntity<List<Flange>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Get flanges by product type",
            description = "Получить все фланцы по типу применяемого продукта",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = TypesFlangesDTO.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/type")
    public ResponseEntity<List<TypesFlangesDTO>> getAllByTypes() {
        return ResponseEntity.ok(service.getFlangesByTypes());
    }

    @Operation(
            summary = "Get flange",
            description = "Получить фланец по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Flange.class))),
                    @ApiResponse(description = "Фланец не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/{id}")
    @JsonView(Views.Full.class)
    public ResponseEntity<Flange> getOne(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Operation(
            summary = "Create flange",
            description = "Создать фланец",
            responses = {
                    @ApiResponse(description = "Успешно создан", responseCode = "201", content = @Content(schema = @Schema(implementation = Flange.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/save")
    public ResponseEntity<Flange> create(@RequestBody Flange flange) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(flange));
    }

    @Operation(
            summary = "Update flange",
            description = "Обновить данные фланца",
            responses = {
                    @ApiResponse(description = "Успешно обновлен", responseCode = "200", content = @Content(schema = @Schema(implementation = Flange.class))),
                    @ApiResponse(description = "Фланец не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PutMapping("/update")
    public ResponseEntity<Flange> update(@RequestBody Flange flange) {
        return ResponseEntity.ok(service.update(flange));
    }

    @Operation(
            summary = "Delete flange",
            description = "Удалить фланец по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешно удален", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Фланец не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Flange flange) {
        service.delete(flange);
        return ResponseEntity.noContent().build();
    }
}
