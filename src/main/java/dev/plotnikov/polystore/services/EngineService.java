package dev.plotnikov.polystore.services;

import dev.plotnikov.polystore.entities.DTOs.ConcatEngineNameDTO;
import dev.plotnikov.polystore.entities.DTOs.SearchProductNameDTO;
import dev.plotnikov.polystore.entities.Engine;
import dev.plotnikov.polystore.repositories.EngineRepository;
import dev.plotnikov.polystore.util.EventType;
import dev.plotnikov.polystore.util.ObjectType;
import dev.plotnikov.polystore.util.WsSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class EngineService {

    private final EngineRepository repository;
    private final BiConsumer<EventType, ConcatEngineNameDTO> wsSenderConcatName;

    public EngineService(EngineRepository repository, WsSender wsSender) {
        this.repository = repository;
        this.wsSenderConcatName = wsSender.getSender(ObjectType.PRODUCT, null);
    }

    public ConcatEngineNameDTO create(Engine engine) {
        final Engine created = repository.save(engine);
        final ConcatEngineNameDTO name = getConcatNameById(created.getId());
        wsSenderConcatName.accept(EventType.CREATE, name);
        return name;
    }

    public ConcatEngineNameDTO getConcatNameById(Long id) {
        return repository.getConcatNameById(id);
    }
    public List<SearchProductNameDTO> findByParams(Map<String, String> map) {

        switch (map.size()) {
            case 1:
                if (map.containsKey("paramOne")) {
                    return repository.findByOneParam(map.get("paramOne"));
                }
                return null;
            case 2:
                if (map.containsKey("paramOne") && map.containsKey("paramTwo")) {
                    return repository.findByTwoParams(map.get("paramOne"), map.get("paramTwo"));
                }
                return null;
            case 3:
                if (map.containsKey("paramOne") && map.containsKey("paramTwo") && map.containsKey("paramThree")) {
                    return repository.findByThreeParams(map.get("paramOne"), map.get("paramTwo"), map.get("paramThree"));
                }
                return null;
            case 4:
                if (map.containsKey("paramOne") && map.containsKey("paramTwo") && map.containsKey("paramThree") && map.containsKey("paramFour")) {
                    return repository.findByFourParams(map.get("paramOne"), map.get("paramTwo"), map.get("paramThree"), map.get("paramFour"));
                }
                return null;
            case 5:
                if (map.containsKey("paramOne") && map.containsKey("paramTwo") && map.containsKey("paramThree") && map.containsKey("paramFour") && map.containsKey("paramFive")) {
                    return repository.findByFiveParams(map.get("paramOne"), map.get("paramTwo"), map.get("paramThree"), map.get("paramFour"), map.get("paramFive"));
                }
                return null;
            default:
                return null;
        }

    }
}

;;
