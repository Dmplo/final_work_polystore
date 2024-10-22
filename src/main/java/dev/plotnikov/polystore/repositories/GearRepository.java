package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.DTOs.ConcatGearNameDTO;
import dev.plotnikov.polystore.entities.DTOs.SearchProductNameDTO;
import dev.plotnikov.polystore.entities.Gear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GearRepository extends JpaRepository<Gear, Long> {
    @Query(value = "SELECT new dev.plotnikov.polystore.entities.DTOs.ConcatGearNameDTO(g.id, g.name, g.ratio, p.name, f.name, s.name, t.id)"
                   + " from Gear g, Size s, Pam p, Flange f, Type t"
                   + " where g.size.id = s.id "
                   + " and g.pam.id = p.id "
                   + " and g.flange.id = f.id "
                   + " and g.type.id = t.id "
                   + " and g.id = :id")
    ConcatGearNameDTO getConcatNameById(Long id);

    @Query(nativeQuery = true, value = """
            select n.name as name, pr.id as id
              from products as pr
              join (
                    select CONCAT(p.name, ' ', s.name, ' ', p.ratio, ' ',pams.name, '/',f.name) as name, p.id as id
                    from products as p
                    left join sizes as s on s.id = p.size_id
                    left join flanges as f on f.id = p.flange_id
                    left join pams on pams.id = p.pam_id
              	) as n on n.id = pr.id
              where pr.type = 'G'
              and LOWER( n.name ) LIKE '%' || :param || '%' 
              """)
    List<SearchProductNameDTO> searchByOneParam(String param);

    @Query(nativeQuery = true, value = """
            select n.name as name, pr.id as id
              from products as pr
              join (
                    select CONCAT(p.name, ' ', s.name, ' ', p.ratio, ' ',pams.name, '/',f.name) as name, p.id as id
                    from products as p
                    left join sizes as s on s.id = p.size_id
                    left join flanges as f on f.id = p.flange_id
                    left join pams on pams.id = p.pam_id
              	) as n on n.id = pr.id
              where pr.type = 'G'
              and LOWER( n.name ) LIKE '%' || :paramOne || '%' 
              and LOWER( n.name ) LIKE '%' || :paramTwo || '%'
              """)
    List<SearchProductNameDTO> searchByTwoParams(String paramOne, String paramTwo);

    @Query(nativeQuery = true, value = """
            select n.name as name, pr.id as id
              from products as pr
              join (
                    select CONCAT(p.name, ' ', s.name, ' ', p.ratio, ' ',pams.name, '/',f.name) as name, p.id as id
                    from products as p
                    left join sizes as s on s.id = p.size_id
                    left join flanges as f on f.id = p.flange_id
                    left join pams on pams.id = p.pam_id
              	) as n on n.id = pr.id
              where pr.type = 'G'
              and LOWER( n.name ) LIKE '%' || :paramOne || '%' 
              and LOWER( n.name ) LIKE '%' || :paramTwo || '%'
              and LOWER( n.name ) LIKE '%' || :paramThree || '%'
              """)
    List<SearchProductNameDTO> searchByThreeParams(String paramOne, String paramTwo, String paramThree);

    @Query(nativeQuery = true, value = """
            select n.name as name, pr.id as id
              from products as pr
              join (
                    select CONCAT(p.name, ' ', s.name, ' ', p.ratio, ' ',pams.name, '/',f.name) as name, p.id as id
                    from products as p
                    left join sizes as s on s.id = p.size_id
                    left join flanges as f on f.id = p.flange_id
                    left join pams on pams.id = p.pam_id
              	) as n on n.id = pr.id
              where pr.type = 'G'
              and LOWER( n.name ) LIKE '%' || :paramOne || '%' 
              and LOWER( n.name ) LIKE '%' || :paramTwo || '%'
              and LOWER( n.name ) LIKE '%' || :paramThree || '%'
              and LOWER( n.name ) LIKE '%' || :paramFour || '%'
              """)
    List<SearchProductNameDTO> searchByFourParams(String paramOne, String paramTwo, String paramThree, String paramFour);

    @Query(nativeQuery = true, value = """
            select n.name as name, pr.id as id
              from products as pr
              join (
                    select CONCAT(p.name, ' ', s.name, ' ', p.ratio, ' ',pams.name, '/',f.name) as name, p.id as id
                    from products as p
                    left join sizes as s on s.id = p.size_id
                    left join flanges as f on f.id = p.flange_id
                    left join pams on pams.id = p.pam_id
              	) as n on n.id = pr.id
              where pr.type = 'G'
              and LOWER( n.name ) LIKE '%' || :paramOne || '%' 
              and LOWER( n.name ) LIKE '%' || :paramTwo || '%'
              and LOWER( n.name ) LIKE '%' || :paramThree || '%'
              and LOWER( n.name ) LIKE '%' || :paramFour || '%'
              and LOWER( n.name ) LIKE '%' || :paramFive || '%'
              """)
    List<SearchProductNameDTO> searchByFiveParams(String paramOne, String paramTwo, String paramThree, String paramFour, String paramFive);

}

;
