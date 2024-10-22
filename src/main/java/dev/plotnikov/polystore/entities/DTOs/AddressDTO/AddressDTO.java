package dev.plotnikov.polystore.entities.DTOs.AddressDTO;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.util.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.aspectj.runtime.internal.Conversions.intValue;
import static org.aspectj.runtime.internal.Conversions.longValue;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class AddressDTO {

    public static final String ID_ALIAS = "a_id";
    public static final String SPACE_ALIAS = "ap_space";
    public static final String NAME_ALIAS = "a_name";

    @JsonView(Views.Id.class)
    private Long addressId;

    @JsonView(Views.MinParams.class)
    private int space;

    @JsonView(Views.MinParams.class)
    private String name;

    @JsonView(Views.Full.class)
    private Set<AddressProductsDTO> products = new HashSet<>();

    public AddressDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        this.addressId = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.space = intValue(tuples[aliasToIndexMap.get(SPACE_ALIAS)]);
        this.name = String.valueOf(tuples[aliasToIndexMap.get(NAME_ALIAS)]);
    }

}