package dev.plotnikov.polystore.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import dev.plotnikov.polystore.entities.DTOs.ReserveDTO;
import dev.plotnikov.polystore.entities.Reserve;
import dev.plotnikov.polystore.entities.User;
import dev.plotnikov.polystore.services.ReserveService;
import dev.plotnikov.polystore.util.Views;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/reserves")
@Tag(name = "Reserve", description = "API для работы с резервами")
@AllArgsConstructor
public class ReserveController {

    private final ReserveService service;

    @Operation(
            summary = "Create address",
            description = "Создать резерв",
            responses = {
                    @ApiResponse(description = "Успешно создан", responseCode = "201", content = @Content(schema = @Schema(implementation = ReserveDTO.class))),
                    @ApiResponse(description = "Ошибка валидации", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/save")
    public ResponseEntity<ReserveDTO> create(@Valid @RequestBody Reserve reserve) {
        ReserveDTO rd = service.create(reserve);
        return ResponseEntity.status(HttpStatus.CREATED).body(rd);
    }

    @Operation(
            summary = "Write off reserve by id",
            description = "Списать резерв по идентификатору",
            responses = {
                    @ApiResponse(description = "Списание прошло успешно", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Резерв не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PutMapping("/{id}/write-off")
    public ResponseEntity<Void> writeOff(@PathVariable Long id) {
        service.writeOff(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete reserve",
            description = "Удалить резерв по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешно удален", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Резерв не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
