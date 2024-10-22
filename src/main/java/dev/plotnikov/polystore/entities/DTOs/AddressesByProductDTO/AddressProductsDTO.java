package dev.plotnikov.polystore.entities.DTOs.AddressesByProductDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.aspectj.runtime.internal.Conversions.*;

@Data
@NoArgsConstructor
public class AddressProductsDTO {
    
    public static final String ADDRESS_PRODUCT_ID_ALIAS = "ap_id";
    public static final String QTY_ALIAS = "ap_qty";
    public static final String UPDATED_AT_ALIAS = "ap_updated_at";
    public static final String SPACE_ALIAS = "ap_space";
    public static final String NAME_ALIAS = "a_name";
    public static final String TYPE_ID_ALIAS = "p_type_id";
    public static final String PRODUCT_ID_ALIAS = "p_id";


    @EqualsAndHashCode.Include
    private Long addressProductId;

    private String name;
    private int qty;
    private String updatedAt;
    private int freeQty;
    private int resQty;
    private Long typeId;
    private Long productId;

    private Set<AddressReservesDTO> reserves = new HashSet<>();

    public AddressProductsDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        int space = intValue(tuples[aliasToIndexMap.get(SPACE_ALIAS)]);
        String nameAdr = String.valueOf(tuples[aliasToIndexMap.get(NAME_ALIAS)]);
        this.addressProductId = longValue(tuples[aliasToIndexMap.get(ADDRESS_PRODUCT_ID_ALIAS)]);
        this.name = String.format("%s-%d", nameAdr, space);
        this.qty = intValue(tuples[aliasToIndexMap.get(QTY_ALIAS)]);
        this.updatedAt = String.valueOf(tuples[aliasToIndexMap.get(UPDATED_AT_ALIAS)]);
        this.freeQty = intValue(tuples[aliasToIndexMap.get(QTY_ALIAS)]);
        this.typeId = longValue(tuples[aliasToIndexMap.get(TYPE_ID_ALIAS)]);
        this.productId = longValue(tuples[aliasToIndexMap.get(PRODUCT_ID_ALIAS)]);
    }
}