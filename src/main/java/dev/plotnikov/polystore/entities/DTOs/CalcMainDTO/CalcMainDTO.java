package dev.plotnikov.polystore.entities.DTOs.CalcMainDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.intValue;
import static org.aspectj.runtime.internal.Conversions.longValue;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class CalcMainDTO {

    public static final String ID_ALIAS = "ap_id";
    public static final String QTY_ALIAS = "ap_qty";

    private Long addressProductId;
    private int qty;
    private int allReserveQty;

    public CalcMainDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        this.addressProductId = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.qty = intValue(tuples[aliasToIndexMap.get(QTY_ALIAS)]);
    }
}