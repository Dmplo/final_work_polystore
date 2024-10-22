package dev.plotnikov.polystore.controllers;

import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import dev.plotnikov.polystore.entities.DTOs.AddressDTO.AddressDTO;
import dev.plotnikov.polystore.entities.DTOs.ProductPageDTO;
import dev.plotnikov.polystore.entities.DTOs.ProductsDTO.TypeProductsDTO;
import dev.plotnikov.polystore.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/products")
@AllArgsConstructor
@Tag(name = "Product", description = "API для работы с продуктами")
public class ProductController {

    private final ProductService service;

    @Operation(
            summary = "Get products by address id",
            description = "Получить все продукты по идентификатору адреса",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = AddressDTO.class))),
                    @ApiResponse(description = "Адрес не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/address/{id}")
    public ResponseEntity<List<AddressDTO>> getProductsByAddressId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProductsByAddressId(id));
    }

    @Operation(
            summary = "Get products by type id",
            description = "Получить все продукты по идентификатору типа",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = TypeProductsDTO.class))),
                    @ApiResponse(description = "Тип не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/type/{id}")
    public ResponseEntity<ProductPageDTO> getProductsByTypeId(
            @PathVariable Long id,
            @PageableDefault(size = 24, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.getProductsByType(id, pageable));
    }

    @Operation(
            summary = "Get products by id",
            description = "Получить продукт по идентификатору типа",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = TypeProductsDTO.class))),
                    @ApiResponse(description = "Продукт не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TypeProductsDTO> getProductsByTypeId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProductById(id));}
}