package dev.plotnikov.polystore.entities.DTOs.AddressesByProductDTO;

import org.hibernate.transform.ResultTransformer;

import java.util.*;

import static org.aspectj.runtime.internal.Conversions.longValue;


public class AddressesByProductDTOResultTransformer implements ResultTransformer {

    private Map<Long, AddressesByProductDTO> addressesByProductDTOMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(
            Object[] tuple,
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = aliasToIndexMap(aliases);
        Long addressId = longValue(tuple[aliasToIndexMap.get(AddressesByProductDTO.ID_ALIAS)]);
        AddressesByProductDTO addressesByProductDTO = addressesByProductDTOMap.computeIfAbsent(
                addressId,
                id -> new AddressesByProductDTO(tuple, aliasToIndexMap)
        );
        AddressProductsDTO addressProductsDTO = new AddressProductsDTO(tuple, aliasToIndexMap);
        if (addressProductsDTO.getAddressProductId() != 0) {
            AddressProductsDTO addressProductChecked = getProductSet(addressesByProductDTO, addressProductsDTO);
            AddressReservesDTO addressReservesDTO = new AddressReservesDTO(tuple, aliasToIndexMap);
            if (addressReservesDTO.getId() != 0) {
                addressProductChecked.setResQty(addressProductChecked.getResQty() + addressReservesDTO.getQty());
                addressProductChecked.setFreeQty(addressProductChecked.getFreeQty() - addressReservesDTO.getQty());
                addressProductChecked.getReserves().add(addressReservesDTO);
            }
        }
        return addressesByProductDTO;
    }

    private AddressProductsDTO getProductSet(AddressesByProductDTO addressesByProductDTO, AddressProductsDTO addressProductsDTO) {
        Optional<AddressProductsDTO> productInSetOpt = addressesByProductDTO.getAddresses().stream().filter(data -> Objects.equals(data.getAddressProductId(), addressProductsDTO.getAddressProductId())).findFirst();
        if (productInSetOpt.isPresent()) {
            return productInSetOpt.get();
        } else {
            addressesByProductDTO.getAddresses().add(addressProductsDTO);
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
        return new ArrayList<>(addressesByProductDTOMap.values());
    }
}
