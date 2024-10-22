package dev.plotnikov.polystore.entities.DTOs.ProductsDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

import static org.aspectj.runtime.internal.Conversions.*;

@Data
@NoArgsConstructor
public class ProductsDTO {

    public static final String ID_ALIAS = "p_id";
    public static final String TYPE_ALIAS = "p_type";
    public static final String NAME_ALIAS = "p_name";
    public static final String RATIO_ALIAS = "p_ratio";
    public static final String SIZE_ALIAS = "p_size";
    public static final String POWER_ALIAS = "p_power";
    public static final String SPEED_ALIAS = "p_speed";
    public static final String FLANGE_ALIAS = "p_flange";
    public static final String PAM_ALIAS = "p_pam";
    public static final String ADR_COUNT_ALIAS = "q_adr_count";


    @EqualsAndHashCode.Include
    private Long id;
    private String type;
    private String name;
    private int addressCount;

    public ProductsDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        String regex = "[,.]?0+$";
        String doubleFormat = "";
        this.id = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.addressCount = intValue(tuples[aliasToIndexMap.get(ADR_COUNT_ALIAS)]);
        this.type = String.valueOf(tuples[aliasToIndexMap.get(TYPE_ALIAS)]);
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