package dev.plotnikov.polystore.entities.DTOs.AddressDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.aspectj.runtime.internal.Conversions.*;
import static org.aspectj.runtime.internal.Conversions.doubleValue;

@Data
@NoArgsConstructor
public class AddressProductsDTO {

    public static final String ID_ALIAS = "p_id";
    public static final String TYPE_ALIAS = "p_type";
    public static final String TYPE_ID_ALIAS = "p_type_id";
    public static final String NAME_ALIAS = "p_name";
    public static final String RATIO_ALIAS = "p_ratio";
    public static final String SIZE_ALIAS = "p_size";
    public static final String POWER_ALIAS = "p_power";
    public static final String SPEED_ALIAS = "p_speed";
    public static final String FLANGE_ALIAS = "p_flange";
    public static final String PAM_ALIAS = "p_pam";
    public static final String ADDRESS_PRODUCT_ID_ALIAS = "ap_id";
    public static final String QTY_ALIAS = "ap_qty";
    public static final String UPDATED_AT_ALIAS = "ap_updated_at";


    @EqualsAndHashCode.Include
    private Long productId;
    private Long typeId;
    private String type;
    private String name;
    private Long addressProductId;
    private int qty;
    private String updatedAt;
    private int resQty;
    private int freeQty;


    private Set<ProductReservesDTO> reserves = new HashSet<>();


    public AddressProductsDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        String regex = "[,.]?0+$";
        String doubleFormat = "";
        this.productId = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.typeId = longValue(tuples[aliasToIndexMap.get(TYPE_ID_ALIAS)]);
        this.qty = intValue(tuples[aliasToIndexMap.get(QTY_ALIAS)]);
        this.freeQty = intValue(tuples[aliasToIndexMap.get(QTY_ALIAS)]);
        this.type = String.valueOf(tuples[aliasToIndexMap.get(TYPE_ALIAS)]);
        this.updatedAt = String.valueOf(tuples[aliasToIndexMap.get(UPDATED_AT_ALIAS)]);
        this.addressProductId = longValue(tuples[aliasToIndexMap.get(ADDRESS_PRODUCT_ID_ALIAS)]);
        String fieldName = String.valueOf(tuples[aliasToIndexMap.get(NAME_ALIAS)]);
        int size = intValue(tuples[aliasToIndexMap.get(SIZE_ALIAS)]);
        String flange = String.valueOf(tuples[aliasToIndexMap.get(FLANGE_ALIAS)]);
        if (Objects.equals(type, "G")) {
            int pam = intValue(tuples[aliasToIndexMap.get(PAM_ALIAS)]);
            double ratio = doubleValue(tuples[aliasToIndexMap.get(RATIO_ALIAS)]);
            if (ratio % 1 != 0) {
                doubleFormat = String.format("%.2f", ratio).replaceAll(regex, "");
            } else {
                doubleFormat = String.format("%.0f", ratio);
            }
            this.name = String.format("%s %d %s %d/%s", fieldName, size, doubleFormat, pam, flange);
        } else if (Objects.equals(type, "E")) {
            double power = doubleValue(tuples[aliasToIndexMap.get(POWER_ALIAS)]);
            if (power > 1) {
                doubleFormat = String.format("%.1f", power).replaceAll(regex, "");
            } else {
                doubleFormat = String.format("%.2f", power);
            }
            int speed = intValue(tuples[aliasToIndexMap.get(SPEED_ALIAS)]);
            this.name = String.format("%s %d %s/%d %s", fieldName, size, doubleFormat, speed, flange);
        }
    }
}