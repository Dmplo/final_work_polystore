package dev.plotnikov.polystore.entities.DTOs.CalcMainDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.intValue;
import static org.aspectj.runtime.internal.Conversions.longValue;

@Data
@NoArgsConstructor
public class CalcQtyProductsDTO {
    public static final String ID_ALIAS = "r_id";
    public static final String RESERVE_ALIAS = "r_qty";

    @EqualsAndHashCode.Include
    private Long id;
    private int resQty;

    public CalcQtyProductsDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        this.id = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.resQty = intValue(tuples[aliasToIndexMap.get(RESERVE_ALIAS)]);
    }
}