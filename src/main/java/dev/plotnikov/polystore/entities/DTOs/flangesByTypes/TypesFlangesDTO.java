package dev.plotnikov.polystore.entities.DTOs.flangesByTypes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.longValue;

@Setter
@Getter
@NoArgsConstructor
public class TypesFlangesDTO {
    public static final String ID_ALIAS = "t_id";

    private Long id;

    private List<FlangesDTO> flanges = new ArrayList<>();

    public TypesFlangesDTO(Object[] tuples, Map<String, Integer> aliasToIndexMap) {
        this.id = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
    }
}