package dev.plotnikov.polystore.entities.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveDTO {
    private Long reserveId;

    private int reserveQty;

    private String comment;

    private LocalDate updatedAt;

    private Long addressProductId;

    private Long addressId;

    private int space;

    private String username;
}