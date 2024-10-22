package dev.plotnikov.polystore.entities.DTOs.AddressesByProductDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.aspectj.runtime.internal.Conversions.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class AddressesByProductDTO {

    public static final String ID_ALIAS = "a_id";
    public static final String NAME_ALIAS = "a_name";
    public static final String TYPE_ALIAS = "p_type";
    public static final String PRODUCT_NAME_ALIAS = "p_name";
    public static final String RATIO_ALIAS = "p_ratio";
    public static final String SIZE_ALIAS = "p_size";
    public static final String POWER_ALIAS = "p_power";
    public static final String SPEED_ALIAS = "p_speed";
    public static final String FLANGE_ALIAS = "p_flange";
    public static final String PAM_ALIAS = "p_pam";

    private Long addressId;
    private String name;
    private String productName;

    private Set<AddressProductsDTO> addresses = new HashSet<>();

    public AddressesByProductDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        this.addressId = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.name = String.valueOf(tuples[aliasToIndexMap.get(NAME_ALIAS)]);
        String regex = "[,.]?0+$";
        String doubleFormat = "";
        String type = String.valueOf(tuples[aliasToIndexMap.get(TYPE_ALIAS)]);
        String fieldName = String.valueOf(tuples[aliasToIndexMap.get(PRODUCT_NAME_ALIAS)]);
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
            this.productName = String.format("%s %d %s %d/%s", fieldName, size, doubleFormat, pam, flange);
        } else if (Objects.equals(type, "E")) {
            double power = doubleValue(tuples[aliasToIndexMap.get(POWER_ALIAS)]);
            if (power > 1) {
                doubleFormat = String.format("%.1f", power).replaceAll(regex, "");
            } else {
                doubleFormat = String.format("%.2f", power);
            }
            int speed = intValue(tuples[aliasToIndexMap.get(SPEED_ALIAS)]);
            this.productName = String.format("%s %d %s/%d %s", fieldName, size, doubleFormat, speed, flange);
        }
    }
}