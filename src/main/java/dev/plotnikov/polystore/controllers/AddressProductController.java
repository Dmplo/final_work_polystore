package dev.plotnikov.polystore.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import dev.plotnikov.polystore.entities.AddressProduct;
import dev.plotnikov.polystore.entities.DTOs.AddressProductDTO;
import dev.plotnikov.polystore.entities.DTOs.AddressProductPageDTO;
import dev.plotnikov.polystore.entities.Pam;
import dev.plotnikov.polystore.services.AddressProductService;
import dev.plotnikov.polystore.util.Views;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/addresses-products")
@AllArgsConstructor
@Tag(name = "AddressProduct", description = "API для работы с продуктами, размещенными по адресу хранения")
public class AddressProductController {
    private final AddressProductService service;

    @Operation(
            summary = "Get addresses with product quantity",
            description = "Получить все адреса с указанием количества хранящихся продуктов",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = AddressProductDTO.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping
    public ResponseEntity<AddressProductPageDTO> getAll(
            @PageableDefault(size = 24, sort = {"name"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(
            summary = "Get addresses with product quantity by id",
            description = "Получить адрес с указанием количества хранящихся продуктов по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Pam.class))),
                    @ApiResponse(description = "Адрес не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AddressProductDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdCustom(id));
    }

    @Operation(
            summary = "Place the product at the storage address",
            description = "Разместить продукт на полку с адресом или увеличить количество уже размещенного продукта",
            responses = {
                    @ApiResponse(description = "Продукт успешно размещен", responseCode = "201", content = @Content(schema = @Schema(implementation = AddressProduct.class))),
                    @ApiResponse(description = "Ошибка валидации", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/add")
    @JsonView(Views.MinParams.class)
    public ResponseEntity<AddressProduct> findOrCreate(@Valid @RequestBody AddressProduct addressProduct) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.findOrCreate(addressProduct));
    }

    @Operation(
            summary = "Write off remaining product at the storage address",
            description = "Полностью или частично списать остатки продукта по адресу хранения",
            responses = {
                    @ApiResponse(description = "Списание прошло успешно", responseCode = "204", content = @Content(schema = @Schema())),
                    @ApiResponse(description = "Адрес хранения продукта не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @PutMapping("/write-off")
    public ResponseEntity<Void> writeOff(@RequestBody() AddressProduct addressProduct) {
        service.updateOrDelete(addressProduct);
        return ResponseEntity.noContent().build();
    }
}
