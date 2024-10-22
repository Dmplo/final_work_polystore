package dev.plotnikov.polystore.entities.DTOs.usersRoles;

import dev.plotnikov.polystore.entities.DTOs.AddressDTO.AddressProductsDTO;
import org.hibernate.transform.ResultTransformer;

import java.util.LinkedHashMap;
import java.util.*;

import static org.aspectj.runtime.internal.Conversions.longValue;

public class UsersRolesDTOResultTransformer implements ResultTransformer {
    private Map<Long, UsersRolesDTO> usersRolesDTOMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(
            Object[] tuple,
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = aliasToIndexMap(aliases);

        Long itemId = longValue(tuple[aliasToIndexMap.get(UsersRolesDTO.ID_ALIAS)]);

        UsersRolesDTO usersRolesDTO = usersRolesDTOMap.computeIfAbsent(
                itemId,
                id -> new UsersRolesDTO(tuple, aliasToIndexMap)
        );

        usersRolesDTO.getRoles().add(
                new RolesDTO(tuple, aliasToIndexMap)
        );
        Optional<RolesDTO> isAdminRole = usersRolesDTO.getRoles().stream().filter(data -> data.getName().equals("ROLE_ADMIN")).findFirst();
        usersRolesDTO.setIsAdmin(isAdminRole.isPresent());
        return usersRolesDTO;
    }

    @Override
    public List transformList(List collection) {
        return new ArrayList<>(usersRolesDTOMap.values());
    }


    public  Map<String, Integer> aliasToIndexMap(
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < aliases.length; i++) {
            aliasToIndexMap.put(aliases[i], i);
        }

        return aliasToIndexMap;
    }
}
