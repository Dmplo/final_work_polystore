package dev.plotnikov.polystore.entities.DTOs.AddressDTO;

import org.hibernate.transform.ResultTransformer;

import java.util.*;

import static org.aspectj.runtime.internal.Conversions.longValue;


public class AddressDTOResultTransformer implements ResultTransformer {

    private Map<Long, AddressDTO> addressDTOMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(
            Object[] tuple,
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = aliasToIndexMap(aliases);
        Long space = longValue(tuple[aliasToIndexMap.get(AddressDTO.SPACE_ALIAS)]);
        AddressDTO addressDTO = addressDTOMap.computeIfAbsent(
                space,
                id -> new AddressDTO(tuple, aliasToIndexMap)
        );
        AddressProductsDTO addressProductsDTO = new AddressProductsDTO(tuple, aliasToIndexMap);
        if (addressProductsDTO.getProductId() != 0) {
            AddressProductsDTO addressProductChecked = getProductSet(addressDTO, addressProductsDTO);
            ProductReservesDTO productReservesDTO = new ProductReservesDTO(tuple, aliasToIndexMap);
            if (productReservesDTO.getId() != 0) {
                addressProductChecked.setResQty(addressProductChecked.getResQty() + productReservesDTO.getQty());
                addressProductChecked.setFreeQty(addressProductChecked.getFreeQty() - productReservesDTO.getQty());
                addressProductChecked.getReserves().add(productReservesDTO);
            }
        }
        return addressDTO;
    }

    private AddressProductsDTO getProductSet(AddressDTO addressDTO, AddressProductsDTO addressProductsDTO) {
        Optional<AddressProductsDTO> productInSetOpt = addressDTO.getProducts().stream().filter(data -> Objects.equals(data.getProductId(), addressProductsDTO.getProductId())).findFirst();
        if (productInSetOpt.isPresent()) {
            return productInSetOpt.get();
        } else {
            addressDTO.getProducts().add(addressProductsDTO);
            return addressProductsDTO;
        }
    }

    public Map<String, Integer> aliasToIndexMap(
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < aliases.length; i++) {
            aliasToIndexMap.put(aliases[i].toLowerCase(), i);
        }

        return aliasToIndexMap;
    }

    @Override
    public List transformList(List resultList) {
        return new ArrayList<>(addressDTOMap.values());
    }
}
