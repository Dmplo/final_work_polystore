package dev.plotnikov.polystore.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.plotnikov.polystore.entities.DTOs.ConcatGearNameDTO;
import dev.plotnikov.polystore.entities.DTOs.SearchProductNameDTO;
import dev.plotnikov.polystore.entities.Gear;
import dev.plotnikov.polystore.entities.IsCheckName;
import dev.plotnikov.polystore.services.GearService;
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
@RequestMapping("api/gears")
@AllArgsConstructor
@Tag(name = "Gear", description = "API для работы с редукторами")
public class GearController {

    private final GearService service;

    @Operation(
            summary = "Find gear",
            description = "Найти редуктор",
            responses = {
                    @ApiResponse(description = "Редуктор найден", responseCode = "200", content = @Content(schema = @Schema(implementation = SearchProductNameDTO.class))),
                    @ApiResponse(description = "Редуктор не найден", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/search")
    public ResponseEntity<List<SearchProductNameDTO>> searchByParams(@RequestBody String payload) throws JsonProcessingException {
        List<SearchProductNameDTO> result = service.searchByParams(JsonToMap.convert(payload));
        return !result.isEmpty() ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Create gear",
            description = "Создать редуктор",
            responses = {
                    @ApiResponse(description = "Успешно создан", responseCode = "201", content = @Content(schema = @Schema(implementation = ConcatGearNameDTO.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/save")
    public ResponseEntity<ConcatGearNameDTO> create(@RequestBody Gear gear) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(gear));
    }

    @Operation(
            summary = "Check gear name",
            description = "Проверить что такой редуктор не создан",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = IsCheckName.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/checkname")
    public ResponseEntity<IsCheckName> isExistsGear(@RequestBody Gear gear) {
        IsCheckName isCheckName = new IsCheckName();
        isCheckName.setIsExists(service.checkGearName(gear));
        return ResponseEntity.ok(isCheckName);
    }
}
