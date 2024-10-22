package dev.plotnikov.polystore.entities.DTOs.ProductsDTO;

import org.hibernate.transform.ResultTransformer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.longValue;

public class TypeProductsDTOResultTransformer implements ResultTransformer {
    private Map<Long, TypeProductsDTO> typeProductsDTOMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(
            Object[] tuple,
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = aliasToIndexMap(aliases);

        Long itemId = longValue(tuple[aliasToIndexMap.get(TypeProductsDTO.ID_ALIAS)]);

        TypeProductsDTO typeProductsDTO = typeProductsDTOMap.computeIfAbsent(
                itemId,
                id -> new TypeProductsDTO(tuple, aliasToIndexMap)
        );

        typeProductsDTO.getProducts().add(
                new ProductsDTO(tuple, aliasToIndexMap)
        );

        return typeProductsDTO;
    }

    @Override
    public List transformList(List collection) {
        return new ArrayList<>(typeProductsDTOMap.values());
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
