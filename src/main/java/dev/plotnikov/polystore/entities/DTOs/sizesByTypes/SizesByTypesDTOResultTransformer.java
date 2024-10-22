package dev.plotnikov.polystore.entities.DTOs.sizesByTypes;

import org.hibernate.transform.ResultTransformer;

import java.util.LinkedHashMap;
import java.util.*;

import static org.aspectj.runtime.internal.Conversions.longValue;

public class SizesByTypesDTOResultTransformer implements ResultTransformer {
    private Map<Long, TypesSizesDTO> typesSizesDTOMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(
            Object[] tuple,
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = aliasToIndexMap(aliases);

        Long itemId = longValue(tuple[aliasToIndexMap.get(TypesSizesDTO.ID_ALIAS)]);

        TypesSizesDTO typesSizesDTO = typesSizesDTOMap.computeIfAbsent(
                itemId,
                id -> new TypesSizesDTO(tuple, aliasToIndexMap)
        );

        typesSizesDTO.getSizes().add(
                new SizesDTO(tuple, aliasToIndexMap)
        );

        return typesSizesDTO;
    }

    @Override
    public List transformList(List collection) {
        return new ArrayList<>(typesSizesDTOMap.values());
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
