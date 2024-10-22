package dev.plotnikov.polystore.entities.DTOs.flangesByTypes;

import org.hibernate.transform.ResultTransformer;

import java.util.LinkedHashMap;
import java.util.*;

import static org.aspectj.runtime.internal.Conversions.longValue;

public class FlangesByTypesDTOResultTransformer implements ResultTransformer {
    private Map<Long, TypesFlangesDTO> typesFlangesDTOMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(
            Object[] tuple,
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = aliasToIndexMap(aliases);

        Long itemId = longValue(tuple[aliasToIndexMap.get(TypesFlangesDTO.ID_ALIAS)]);

        TypesFlangesDTO typesFlangesDTO = typesFlangesDTOMap.computeIfAbsent(
                itemId,
                id -> new TypesFlangesDTO(tuple, aliasToIndexMap)
        );

        typesFlangesDTO.getFlanges().add(
                new FlangesDTO(tuple, aliasToIndexMap)
        );

        return typesFlangesDTO;
    }

    @Override
    public List transformList(List collection) {
        return new ArrayList<>(typesFlangesDTOMap.values());
    }


    public  Map<String, Integer> aliasToIndexMap(
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < aliases.length; i++) {
            aliasToIndexMap.put(aliases[i], i);
        }

        return aliasToIndexMap;
    }
}
