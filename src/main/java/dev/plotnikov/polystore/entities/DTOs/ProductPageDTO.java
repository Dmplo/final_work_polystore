package dev.plotnikov.polystore.entities.DTOs;

import dev.plotnikov.polystore.entities.DTOs.ProductsDTO.TypeProductsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductPageDTO {
    TypeProductsDTO typeProducts;
    private int currentPage;
    private long totalPages;
}
