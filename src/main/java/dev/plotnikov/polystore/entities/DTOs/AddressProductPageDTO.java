package dev.plotnikov.polystore.entities.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddressProductPageDTO {
    List<AddressProductDTO> addresses;
    private int currentPage;
    private int totalPages;
}
