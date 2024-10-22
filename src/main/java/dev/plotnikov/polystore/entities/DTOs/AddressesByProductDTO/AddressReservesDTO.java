package dev.plotnikov.polystore.entities.DTOs.AddressesByProductDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.intValue;
import static org.aspectj.runtime.internal.Conversions.longValue;

@Data
@NoArgsConstructor
public class AddressReservesDTO {

    public static final String ID_ALIAS = "r_id";
    public static final String COMMENT_ALIAS = "r_comment";
    public static final String QTY_ALIAS = "r_qty";
    public static final String UPDATED_AT_ALIAS = "r_updated_at";
    public static final String USER_NAME_ALIAS = "u_name";


    @EqualsAndHashCode.Include
    private Long id;
    private String comment;
    private int qty;
    private String updatedAt;
    private String username;

    public AddressReservesDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        this.id = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.qty = intValue(tuples[aliasToIndexMap.get(QTY_ALIAS)]);
        this.comment = String.valueOf(tuples[aliasToIndexMap.get(COMMENT_ALIAS)]);
        this.updatedAt = String.valueOf(tuples[aliasToIndexMap.get(UPDATED_AT_ALIAS)]);
        this.username = String.valueOf(tuples[aliasToIndexMap.get(USER_NAME_ALIAS)]);
    }

}