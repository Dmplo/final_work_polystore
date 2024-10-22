package dev.plotnikov.polystore.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.plotnikov.polystore.entities.DTOs.ConcatEngineNameDTO;
import dev.plotnikov.polystore.entities.DTOs.SearchProductNameDTO;
import dev.plotnikov.polystore.entities.Engine;
import dev.plotnikov.polystore.services.EngineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/engines")
@AllArgsConstructor
@Tag(name = "Engine", description = "API для работы с двигателями")
public class EngineController {

    private final EngineService service;

    @Operation(
            summary = "Find engine",
            description = "Найти двигатель",
            responses = {
                    @ApiResponse(description = "Двигатель найден", responseCode = "200", content = @Content(schema = @Schema(implementation = SearchProductNameDTO.class))),
                    @ApiResponse(description = "Двигатель не найден", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/search")
    public ResponseEntity<List<SearchProductNameDTO>> findByParams(@RequestBody String payload) throws JsonProcessingException {
        List<SearchProductNameDTO> result = service.findByParams(JsonToMap.convert(payload));
        return !result.isEmpty() ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Create engine",
            description = "Создать двигатель",
            responses = {
                    @ApiResponse(description = "Успешно создан", responseCode = "201", content = @Content(schema = @Schema(implementation = ConcatEngineNameDTO.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/save")
    public ResponseEntity<ConcatEngineNameDTO> create(@RequestBody Engine engine) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(engine));
    }

}
