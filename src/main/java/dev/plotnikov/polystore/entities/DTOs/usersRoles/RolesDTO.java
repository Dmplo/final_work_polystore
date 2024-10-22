package dev.plotnikov.polystore.entities.DTOs.usersRoles;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.longValue;

@Setter
@Getter
@NoArgsConstructor
public class RolesDTO {
    public static final String ID_ALIAS = "r_id";
    public static final String NAME_ALIAS = "r_name";


    private Long id;
    private String name;

    public RolesDTO(Object[] tuples, Map<String, Integer> aliasToIndexMap) {
        this.id = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.name = String.valueOf(tuples[aliasToIndexMap.get(NAME_ALIAS)]);
    }

    @Override
    public String toString() {
        return "RolesDTO{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
