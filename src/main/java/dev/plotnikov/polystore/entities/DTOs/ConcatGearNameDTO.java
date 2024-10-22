package dev.plotnikov.polystore.entities.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ConcatGearNameDTO {
    private Long id;
    private String name;
    private Long typeId;

    public ConcatGearNameDTO(Long id, String name, Double ratio, int pam, int flange, int size, Long typeId) {
        this.id = id;
        this.name = String.format("%s %d %.1f PAM %d/%s", name, size, ratio, pam, flange);
        this.typeId = typeId;
    }

}