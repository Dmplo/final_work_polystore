package dev.plotnikov.polystore.entities.DTOs.ProductsDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.longValue;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class TypeProductsDTO {

    public static final String ID_ALIAS = "t_id";
    public static final String NAME_ALIAS = "t_name";

    private Long id;
    private String name;

    private List<ProductsDTO> products = new ArrayList<>();

    public TypeProductsDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        this.id = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.name = String.valueOf(tuples[aliasToIndexMap.get(NAME_ALIAS)]);
    }
}