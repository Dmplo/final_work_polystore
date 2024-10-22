package dev.plotnikov.polystore.entities.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressProductDTO {

    private Long id;
    private String name;
    private int capacity;
    private int addressProductQty;
}