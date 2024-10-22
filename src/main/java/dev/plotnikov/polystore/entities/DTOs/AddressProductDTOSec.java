package dev.plotnikov.polystore.entities.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressProductDTOSec {

    private Long id;
    private String addressName;
    private Long productTypeId;
    private Long productId;
    private int qty;
    private int space;
}