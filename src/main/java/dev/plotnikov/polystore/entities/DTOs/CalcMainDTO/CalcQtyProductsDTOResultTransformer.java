package dev.plotnikov.polystore.entities.DTOs.CalcMainDTO;

import org.hibernate.transform.ResultTransformer;
import java.util.*;
import static org.aspectj.runtime.internal.Conversions.longValue;

public class CalcQtyProductsDTOResultTransformer implements ResultTransformer {

    private Map<Long, CalcMainDTO> calcQtyDTOMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(
            Object[] tuple,
            String[] aliases) {
        Map<String, Integer> aliasToIndexMap = aliasToIndexMap(aliases);
        Long itemId = longValue(tuple[aliasToIndexMap.get(CalcMainDTO.ID_ALIAS)]);
        CalcMainDTO calcMainDTO = calcQtyDTOMap.computeIfAbsent(
                itemId,
                id -> new CalcMainDTO(tuple, aliasToIndexMap)
        );
        CalcQtyProductsDTO calcQtyProductsDTO = new CalcQtyProductsDTO(tuple, aliasToIndexMap);
        if (calcQtyProductsDTO.getId() != 0) {
            calcMainDTO.setAllReserveQty(calcMainDTO.getAllReserveQty() + calcQtyProductsDTO.getResQty());
        }
        return calcMainDTO;
    }

    public Map<String, Integer> aliasToIndexMap(
            String[] aliases) {
        Map<String, Integer> aliasToIndexMap = new LinkedHashMap<>();
        for (int i = 0; i < aliases.length; i++) {
            aliasToIndexMap.put(aliases[i].toLowerCase(), i);
        }
        return aliasToIndexMap;
    }

    @Override
    public List transformList(List resultList) {
        return new ArrayList<>(calcQtyDTOMap.values());
    }
}
