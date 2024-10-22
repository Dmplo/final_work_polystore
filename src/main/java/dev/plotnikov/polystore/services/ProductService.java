package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.DTOs.AddressDTO.AddressDTO;
import dev.plotnikov.polystore.entities.DTOs.AddressDTO.AddressDTOResultTransformer;
import dev.plotnikov.polystore.entities.DTOs.ProductPageDTO;
import dev.plotnikov.polystore.entities.DTOs.ProductsDTO.TypeProductsDTO;
import dev.plotnikov.polystore.entities.DTOs.ProductsDTO.TypeProductsDTOResultTransformer;
import dev.plotnikov.polystore.repositories.AddressRepository;
import dev.plotnikov.polystore.repositories.ProductRepository;
import dev.plotnikov.polystore.repositories.TypeRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProductService {

    private final EntityManager entityManager;
    private final TypeRepository typeRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    public List<AddressDTO> getProductsByAddressId(Long id) {
        if (addressRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Адреса с id = " + id + " не существует!");
        }
        return entityManager.createNativeQuery("""
                            select
                            p.id as p_id,
                            p.type as p_type,
                            p.type_id as p_type_id,
                            p.name as p_name,
                            p.ratio as p_ratio,
                            s.name as p_size,
                            pw.name as p_power,
                            sp.name as p_speed,
                            f.name as p_flange,
                            pam.name as p_pam,
                            r.id as r_id,
                            CONCAT (u.firstname, ' ', u.lastname) as u_name,
                            r.comment as r_comment,
                            r.qty as r_qty,
                            r.updated_at as r_updated_at,
                            a.name as a_name,
                            a.id as a_id,
                            ap.id as ap_id,
                            ap.space as ap_space,
                            ap.updated_at as ap_updated_at,
                            ap.qty as ap_qty
                            from addresses a
                            left join addresses_products ap on ap.address_id = a.id
                            left join products p on p.id = ap.product_id
                            left join reserves r on r.address_id = ap.id
                            left join sizes s on p.size_id = s.id
                            left join flanges f on p.flange_id = f.id
                            left join powers pw on p.power_id = pw.id
                            left join speeds sp on p.speed_id = sp.id
                            left join pams pam on p.pam_id = pam.id
                            left join users u on r.user_id = u.id
                            where a.id = ?1
                            order by ap.space
                        """).setParameter(1, id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AddressDTOResultTransformer())
                .getResultList();
    }

    public ProductPageDTO getProductsByType(Long id, Pageable pageable) {
        if (typeRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Типа с id = " + id + " не существует!");
        }
        Long productsCount = productRepository.findProductsCount(id);
        long totalCount = Math.max(pageable.getPageSize(), productsCount);
        Page<TypeProductsDTO> pageableRes = new PageImpl<>(List.of(), pageable, totalCount);
        ProductPageDTO result = new ProductPageDTO(null, pageable.getPageNumber(), pageableRes.getTotalPages());

        String stringQuery = getStringQuery(id, "p.type_id");
        if (pageable.getPageNumber() <= pageableRes.getTotalPages()) {
            List<TypeProductsDTO> resultList = entityManager.createNativeQuery(stringQuery).setParameter(1, id).setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize())
                    .unwrap(org.hibernate.query.Query.class)
                    .setResultTransformer(new TypeProductsDTOResultTransformer())
                    .getResultList();

            if (!resultList.isEmpty()) {
                result.setTypeProducts(resultList.get(0));
            } else {
                throw new IllegalArgumentException("Не найдено продуктов с типом id = " + id);
            }
        }
        return result;
    }

    private static String getStringQuery(Long id, String param) {
        String stringQuery = String.format("""
                select
                p.id as p_id,
                p.type as p_type,
                p.name as p_name,
                p.ratio as p_ratio,
                s.name as p_size,
                t.id as t_id,
                t.name as t_name,
                pw.name as p_power,
                sp.name as p_speed,
                f.name as p_flange,
                pam.name as p_pam,
                q.adr_count as q_adr_count
                from products p
                left join sizes s on p.size_id=s.id
                left join types t on p.type_id=t.id
                left join flanges f on p.flange_id=f.id
                left join powers pw on p.power_id=pw.id
                left join speeds sp on p.speed_id=sp.id
                left join pams pam on p.pam_id=pam.id
                left join (select ap.product_id, count(ap.id) as adr_count 
                from addresses_products ap group by ap.product_id) q on p.id=q.product_id
                where %s = ?1""", param);
        if (Objects.equals(param, "p.type_id")) {
            stringQuery += " order by s.name";
            if (id == 1) {
                stringQuery += ", p.ratio, pam.name, f.name";
            } else if (id == 2) {
                stringQuery += ", pw.name, f.name";
            }
            stringQuery += ", p.id";
        }
        return stringQuery;
    }

    public TypeProductsDTO getProductById(Long id) {
        if (productRepository.findProductCustom(id).isEmpty()) {
            throw new NoSuchElementException("Продукта с id = " + id + " не существует!");
        }
        String stringQuery = getStringQuery(id, "p.id");
        return (TypeProductsDTO) entityManager.createNativeQuery(stringQuery).setParameter(1, id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new TypeProductsDTOResultTransformer())
                .uniqueResult();
    }


}


