package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.DTOs.ConcatGearNameDTO;
import dev.plotnikov.polystore.entities.DTOs.SearchProductNameDTO;
import dev.plotnikov.polystore.entities.Gear;
import dev.plotnikov.polystore.repositories.GearRepository;
import dev.plotnikov.polystore.util.EventType;
import dev.plotnikov.polystore.util.ObjectType;
import dev.plotnikov.polystore.util.WsSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class GearService {

    private final GearRepository repository;
    private final BiConsumer<EventType, ConcatGearNameDTO> wsSenderConcatName;

    public GearService(GearRepository repository, WsSender wsSender) {
        this.repository = repository;
        this.wsSenderConcatName = wsSender.getSender(ObjectType.PRODUCT, null);
    }

    public ConcatGearNameDTO create(Gear gear) {
        final Gear created = repository.save(gear);
        ConcatGearNameDTO name = getConcatNameById(created.getId());
        wsSenderConcatName.accept(EventType.CREATE, name);
        return name;
    }

    public ConcatGearNameDTO getConcatNameById(Long id) {
        return repository.getConcatNameById(id);
    }

    public Boolean checkGearName(Gear gear) {
        return repository.checkGearName(gear.getName(), gear.getType().getId(), gear.getSize().getId(), gear.getRatio(), gear.getPam().getId(), gear.getFlange().getId()).isPresent();
    }


    public List<SearchProductNameDTO> searchByParams(Map<String, String> map) {
        switch (map.size()) {
            case 1:
                if (map.containsKey("paramOne")) {
                    return repository.searchByOneParam(map.get("paramOne"));
                }
                return null;
            case 2:
                if (map.containsKey("paramOne") && map.containsKey("paramTwo")) {
                    return repository.searchByTwoParams(map.get("paramOne"), map.get("paramTwo"));
                }
                return null;
            case 3:
                if (map.containsKey("paramOne") && map.containsKey("paramTwo") && map.containsKey("paramThree")) {
                    return repository.searchByThreeParams(map.get("paramOne"), map.get("paramTwo"), map.get("paramThree"));
                }
                return null;
            case 4:
                if (map.containsKey("paramOne") && map.containsKey("paramTwo") && map.containsKey("paramThree") && map.containsKey("paramFour")) {
                    return repository.searchByFourParams(map.get("paramOne"), map.get("paramTwo"), map.get("paramThree"), map.get("paramFour"));
                }
                return null;
            case 5:
                if (map.containsKey("paramOne") && map.containsKey("paramTwo") && map.containsKey("paramThree") && map.containsKey("paramFour") && map.containsKey("paramFive")) {
                    return repository.searchByFiveParams(map.get("paramOne"), map.get("paramTwo"), map.get("paramThree"), map.get("paramFour"), map.get("paramFive"));
                }
                return null;
            default:
                return null;
        }
    }
}