package dev.plotnikov.polystore.entities.DTOs.sizesByTypes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.longValue;

@Setter
@Getter
@NoArgsConstructor
public class SizesDTO {
    public static final String ID_ALIAS = "s_id";
    public static final String NAME_ALIAS = "s_name";

    private Long id;
    private String name;

    public SizesDTO(Object[] tuples, Map<String, Integer> aliasToIndexMap) {
        this.id = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.name = String.valueOf(tuples[aliasToIndexMap.get(NAME_ALIAS)]);
    }
}
