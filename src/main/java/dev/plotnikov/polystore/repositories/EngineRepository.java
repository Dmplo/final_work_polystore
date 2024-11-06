package dev.plotnikov.polystore.repositories;

import dev.plotnikov.polystore.entities.DTOs.ConcatEngineNameDTO;
import dev.plotnikov.polystore.entities.DTOs.SearchProductNameDTO;
import dev.plotnikov.polystore.entities.Engine;
import dev.plotnikov.polystore.entities.Gear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EngineRepository extends JpaRepository<Engine, Long> {

    @Query(value = "SELECT new dev.plotnikov.polystore.entities.DTOs.ConcatEngineNameDTO(e.id, e.name, p.name, sp.name, f.name, s.name, t.id)"
                   + " from Engine e, Size s, Power p, Speed sp, Flange f, Type t"
                   + " where e.size.id = s.id "
                   + " and e.power.id = p.id "
                   + " and e.speed.id = sp.id "
                   + " and e.flange.id = f.id "
                   + " and e.type.id = t.id "
                   + " and e.id = :id")
    ConcatEngineNameDTO getConcatNameById(Long id);

    @Query(nativeQuery = true, value = """
            select n.name as name, pr.id as id
              from products as pr
              join (
                    select CONCAT(p.name, ' ', s.name, ' ', pw.name, '/',sp.name, ' ',f.name) as name, p.id as id
                    from products as p
                    left join sizes as s on s.id = p.size_id
                    left join flanges as f on f.id = p.flange_id
                    left join speeds as sp on sp.id = p.speed_id
                    left join powers as pw on pw.id = p.power_id
              	) as n on n.id = pr.id
              where pr.type = 'E'
              and LOWER( n.name ) LIKE '%' || :param || '%' 
              """)
    List<SearchProductNameDTO> findByOneParam(String param);

    @Query(nativeQuery = true, value = """
            select n.name as name, pr.id as id
              from products as pr
              join (
                    select CONCAT(p.name, ' ', s.name, ' ', pw.name, '/', sp.name, ' ',f.name) as name, p.id as id
                    from products as p
                    left join sizes as s on s.id = p.size_id
                    left join flanges as f on f.id = p.flange_id
                    left join speeds as sp on sp.id = p.speed_id
                    left join powers as pw on pw.id = p.power_id
              	) as n on n.id = pr.id
              where pr.type = 'E'
              and LOWER( n.name ) LIKE '%' || :paramOne || '%' 
              and LOWER( n.name ) LIKE '%' || :paramTwo || '%'
              """)
    List<SearchProductNameDTO> findByTwoParams(String paramOne, String paramTwo);

    @Query(nativeQuery = true, value = """
            select n.name as name, pr.id as id
              from products as pr
              join (
                    select CONCAT(p.name, ' ', s.name, ' ', pw.name, '/',sp.name, ' ',f.name) as name, p.id as id
                    from products as p
                    left join sizes as s on s.id = p.size_id
                    left join flanges as f on f.id = p.flange_id
                    left join speeds as sp on sp.id = p.speed_id
                    left join powers as pw on pw.id = p.power_id
              	) as n on n.id = pr.id
              where pr.type = 'E'
              and LOWER( n.name ) LIKE '%' || :paramOne || '%' 
              and LOWER( n.name ) LIKE '%' || :paramTwo || '%'
              and LOWER( n.name ) LIKE '%' || :paramThree || '%'
              """)
    List<SearchProductNameDTO> findByThreeParams(String paramOne, String paramTwo, String paramThree);

    @Query(nativeQuery = true, value = """
            select n.name as name, pr.id as id
              from products as pr
              join (
                    select CONCAT(p.name, ' ', s.name, ' ', pw.name, '/',sp.name, ' ',f.name) as name, p.id as id
                    from products as p
                    left join sizes as s on s.id = p.size_id
                    left join flanges as f on f.id = p.flange_id
                    left join speeds as sp on sp.id = p.speed_id
                    left join powers as pw on pw.id = p.power_id
              	) as n on n.id = pr.id
              where pr.type = 'E'
              and LOWER( n.name ) LIKE '%' || :paramOne || '%' 
              and LOWER( n.name ) LIKE '%' || :paramTwo || '%'
              and LOWER( n.name ) LIKE '%' || :paramThree || '%'
              and LOWER( n.name ) LIKE '%' || :paramFour || '%'
              """)
    List<SearchProductNameDTO> findByFourParams(String paramOne, String paramTwo, String paramThree, String paramFour);

    @Query(nativeQuery = true, value = """
            select n.name as name, pr.id as id
              from products as pr
              join (
                    select CONCAT(p.name, ' ', s.name, ' ', pw.name, '/',sp.name, ' ',f.name) as name, p.id as id
                    from products as p
                    left join sizes as s on s.id = p.size_id
                    left join flanges as f on f.id = p.flange_id
                    left join speeds as sp on sp.id = p.speed_id
                    left join powers as pw on pw.id = p.power_id
              	) as n on n.id = pr.id
              where pr.type = 'E'
              and LOWER( n.name ) LIKE '%' || :paramOne || '%' 
              and LOWER( n.name ) LIKE '%' || :paramTwo || '%'
              and LOWER( n.name ) LIKE '%' || :paramThree || '%'
              and LOWER( n.name ) LIKE '%' || :paramFour || '%'
              and LOWER( n.name ) LIKE '%' || :paramFive || '%'
              """)
    List<SearchProductNameDTO> findByFiveParams(String paramOne, String paramTwo, String paramThree, String paramFour, String paramFive);

    @Query(nativeQuery = true, value = """
            select *
              from products as p
              where p.name = :name
              and p.type_id = :typeId
              and p.size_id = :sizeId
              and p.power_id = :powerId
              and p.speed_id = :speedId
              and p.flange_id = :flangeId
              """)
    Optional<Engine> checkEngineName(String name, Long typeId, Long sizeId, Long powerId, Long speedId, Long flangeId);
}

;
