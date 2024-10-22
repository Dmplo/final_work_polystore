package dev.plotnikov.polystore.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import dev.plotnikov.polystore.entities.Address;
import dev.plotnikov.polystore.entities.DTOs.AddressesByProductDTO.AddressesByProductDTO;
import dev.plotnikov.polystore.entities.IsCheckName;
import dev.plotnikov.polystore.services.AddressService;
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
@RequestMapping("api/addresses")
@AllArgsConstructor
@Tag(name = "Addresses", description = "API для работы с адресами")
public class AddressController {
    private final AddressService service;

    @Operation(
            summary = "Get addresses",
            description = "Получить все адреса",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Address.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping
    @JsonView(Views.MinParams.class)
    public ResponseEntity<List<Address>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Get addresses by product id",
            description = "Получить все адреса по идентификатору продукта",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = AddressesByProductDTO.class))),
                    @ApiResponse(description = "Адрес не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/product/{id}")
    public ResponseEntity<List<AddressesByProductDTO>> findAddressesByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findAddressesByProductId(id));
    }

    @Operation(
            summary = "Create address",
            description = "Создать адрес",
            responses = {
                    @ApiResponse(description = "Успешно создан", responseCode = "201", content = @Content(schema = @Schema(implementation = Address.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/save")
    public ResponseEntity<Address> create(@RequestBody Address address) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(address));
    }

    @Operation(
            summary = "Check address name",
            description = "Проверить что название адреса не занято",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = IsCheckName.class))),
                    @ApiResponse(description = "Название адреса не найдено", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/checkname/{name}")
    public ResponseEntity<IsCheckName> isExistsAddressName(@PathVariable String name) {
        IsCheckName isCheckName = new IsCheckName();
        isCheckName.setIsExists(service.checkAddressName(name));
        return ResponseEntity.ok(isCheckName);
    }
}
