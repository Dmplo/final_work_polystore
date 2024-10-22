package dev.plotnikov.polystore.entities.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ConcatEngineNameDTO {
    private Long id;
    private String name;
    private Long typeId;

    public ConcatEngineNameDTO(Long id, String name, Double power, int speed, int flange, int size, Long typeId) {
        this.id = id;
        this.name = String.format("%s %d %.1f PAM %d/%s", name, size, power, speed, flange);
        this.typeId = typeId;
    }

}