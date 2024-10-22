package dev.plotnikov.polystore;

import dev.plotnikov.polystore.entities.*;
import dev.plotnikov.polystore.repositories.*;
import dev.plotnikov.polystore.services.RoleService;
import dev.plotnikov.polystore.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class PolystoreApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(PolystoreApplication.class, args);

        AddressProductRepository addressProductRepo = ctx.getBean(AddressProductRepository.class);
        SizeTypeRepository sizeTypeRepo = ctx.getBean(SizeTypeRepository.class);
        AddressRepository addressRepo = ctx.getBean(AddressRepository.class);
        GearRepository gearRepo = ctx.getBean(GearRepository.class);
        EngineRepository engineRepo = ctx.getBean(EngineRepository.class);
        PamRepository pamRepo = ctx.getBean(PamRepository.class);
        SizeRepository sizeRepo = ctx.getBean(SizeRepository.class);
        TypeRepository typeRepo = ctx.getBean(TypeRepository.class);
        FlangeRepository flangeRepo = ctx.getBean(FlangeRepository.class);
        PowerRepository powerRepo = ctx.getBean(PowerRepository.class);
        SpeedRepository speedRepo = ctx.getBean(SpeedRepository.class);
        ReserveRepository reserveRepo = ctx.getBean(ReserveRepository.class);
        UserService userService = ctx.getBean(UserService.class);
        RoleService roleService = ctx.getBean(RoleService.class);

        createUsers(userService, roleService);

        List<User> users = userService.getUsers();

        Map<Integer, List<Double>> ratio = new HashMap<>();
        Map<Integer, List<Integer>> pam = new HashMap<>();
        Map<Integer, List<Integer>> flange = new HashMap<>();
        Map<Integer, List<Double>> power = new HashMap<>();
        Map<Integer, List<Integer>> speed = new HashMap<>();


// Params Gears
        List<Integer> gearSizeParamList = List.of(30, 40, 50, 63, 75, 90, 110, 130, 150);
        List<Integer> pamParamList = List.of(9, 11, 14, 19, 22, 24, 28, 38, 42);
        List<Integer> gearFlangeParamList = List.of(80, 90, 105, 120, 140, 160, 200, 250, 300, 350);
        List<Pam> pamsList = new ArrayList<>();
        List<Flange> flangeList = new ArrayList<>();

        for (Integer integer : pamParamList) {
            Pam pam2 = new Pam(integer);
            pamRepo.save(pam2);
            pamsList.add(pam2);
        }

        ratio.put(30, List.of(5.0, 7.5, 10.0, 15.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 80.0));
        pam.put(30, List.of(9, 11));
        flange.put(30, List.of(80, 90, 120, 140));

        ratio.put(40, List.of(5.0, 7.5, 10.0, 15.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0));
        pam.put(40, List.of(11, 14));
        flange.put(40, List.of(80, 90, 105, 120, 140, 160));

        ratio.put(50, List.of(5.0, 7.5, 10.0, 15.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0));
        pam.put(50, List.of(11, 14, 19));
        flange.put(50, List.of(90, 105, 120, 140, 160, 200));

        ratio.put(63, List.of(7.5, 10.0, 15.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0));
        pam.put(63, List.of(14, 19, 22, 24));
        flange.put(63, List.of(90, 105, 120, 140, 160, 200));

        ratio.put(75, List.of(7.5, 10.0, 15.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0));
        pam.put(75, List.of(14, 19, 22, 24, 28));
        flange.put(75, List.of(120, 140, 160, 200, 250));

        ratio.put(90, List.of(7.5, 10.0, 15.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0));
        pam.put(90, List.of(19, 22, 24, 28));
        flange.put(90, List.of(120, 140, 160, 200, 250));

        ratio.put(110, List.of(7.5, 10.0, 15.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0));
        pam.put(110, List.of(22, 24, 28, 38));
        flange.put(110, List.of(140, 160, 200, 250, 300, 350));

        ratio.put(130, List.of(7.5, 10.0, 15.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0));
        pam.put(130, List.of(24, 28, 38));
        flange.put(130, List.of(140, 160, 200, 250, 300, 350));

        ratio.put(150, List.of(7.5, 10.0, 15.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0));
        pam.put(150, List.of(28, 38, 42));
        flange.put(150, List.of(200, 250, 300, 350));


// Params Engines
        List<Integer> engineSizeParamList = List.of(56, 63, 71, 80, 90, 100, 112, 132);
        List<Double> enginePowerParamList = List.of(0.09, 0.12, 0.18, 0.25, 0.37, 0.55, 0.75, 1.1, 1.5, 2.2, 3.0, 4.0, 5.5, 7.5, 11.0);
        List<Integer> engineSpeedParamList = List.of(750, 1000, 1500, 3000);
        List<Integer> engineFlangeParamList = List.of(1081, 2081, 2181, 3081, 3681);
        List<String> engineNames = List.of("AИР", "MS", "YEJ");
        List<Power> powersEngineList = new ArrayList<>();
        List<Speed> speedEngineList = new ArrayList<>();
        List<Flange> flangeEngineList = new ArrayList<>();

        power.put(56, List.of(0.09, 0.12, 0.18, 0.25));
        speed.put(56, List.of(1500, 3000));

        power.put(63, List.of(0.09, 0.12, 0.18, 0.25, 0.37, 0.55));
        speed.put(63, List.of(1000, 1500, 3000));

        power.put(71, List.of(0.12, 0.18, 0.25, 0.37, 0.55, 0.75, 1.1));
        speed.put(71, List.of(750, 1000, 1500, 3000));

        power.put(80, List.of(0.18, 0.25, 0.37, 0.55, 0.75, 1.1, 1.5, 2.2));
        speed.put(80, List.of(750, 1000, 1500, 3000));

        power.put(90, List.of(0.75, 1.1, 1.5, 2.2, 3.0));
        speed.put(90, List.of(750, 1000, 1500, 3000));

        power.put(100, List.of(1.5, 2.2, 3.0, 4.0, 5.5));
        speed.put(100, List.of(750, 1000, 1500, 3000));

        power.put(112, List.of(2.2, 3.0, 4.0, 5.5, 7.5));
        speed.put(112, List.of(750, 1000, 1500, 3000));

        power.put(132, List.of(3.0, 4.0, 5.5, 7.5, 11.0));
        speed.put(132, List.of(750, 1000, 1500, 3000));

        for (Double aDouble : enginePowerParamList) {
            Power power1 = new Power(aDouble);
            powerRepo.save(power1);
            powersEngineList.add(power1);
        }
        for (Integer integer : engineSpeedParamList) {
            Speed speed1 = new Speed(integer);
            speedRepo.save(speed1);
            speedEngineList.add(speed1);
        }

        List<Integer> sizesNamesList = List.of(30, 40, 50, 56, 63, 71, 75, 80, 90, 100, 110, 112, 130, 132, 150);
        List<Size> sizesList = new ArrayList<>();
        for (Integer integer : sizesNamesList) {
            Size size = new Size(integer);
            sizeRepo.save(size);
            sizesList.add(size);
        }

        List<String> typesNames = List.of("gears", "engines");
        List<Type> typeList = new ArrayList<>();
        Map<Type, List<Size>> typeSizeMap = new HashMap<>();

        for (String string : typesNames) {
            Type type = new Type(string);
            typeRepo.save(type);
            typeList.add(type);
            if (string.equals("gears")) {
                typeSizeMap.put(type, getTypeSizeMap(gearSizeParamList, sizesList, type, sizeTypeRepo));
            } else if (string.equals("engines")) {
                typeSizeMap.put(type, getTypeSizeMap(engineSizeParamList, sizesList, type, sizeTypeRepo));
            }
        }

        for (Integer integer : gearFlangeParamList) {
            Type type = typeList.stream()
                    .filter(it -> Objects.equals(it.getName(), "gears"))
                    .findFirst().get();
            Flange flange1 = new Flange(integer, type);
            flangeRepo.save(flange1);
            flangeList.add(flange1);
        }

        for (Integer integer : engineFlangeParamList) {
            Type type = typeList.stream()
                    .filter(it -> Objects.equals(it.getName(), "engines"))
                    .findFirst().get();
            Flange flange1 = new Flange(integer, type);
            flangeRepo.save(flange1);
            flangeEngineList.add(flange1);
        }

        Set<Gear> gearsSet = new HashSet<>();
        Set<Engine> enginesSet = new HashSet<>();

        List<String> addressesName = List.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Address>> addressesList = new HashMap<>();
        for (int i = 0; i < addressesName.size(); i++) {
            List<Address> adr = new ArrayList<>();
            for (int j = 1; j <= 10; j++) {
                Address address = new Address(addressesName.get(i) + j, ThreadLocalRandom.current().nextInt(3, 7));
                addressRepo.save(address);
                adr.add(address);
            }
            addressesList.put(addressesName.get(i), adr);
        }

        List<AddressProduct> addressProducts = new ArrayList<>();

        for (int i = 1; i <= 200; i++) {

            Type rndType = getRandom(typeList);
            Size currentSize = getRandom(typeSizeMap.get(rndType));

            if (rndType.getName().equals("gears")) {
                int currentPamValue = getRandom(pam.get(currentSize.getName()));
                Pam currentPam = pamsList.stream().filter(n -> Objects.equals(n.getName(), currentPamValue)).findFirst().get();

                int currentFlangeValue = getRandom(flange.get(currentSize.getName()));
                Flange currentFlange = flangeList.stream().filter(n -> Objects.equals(n.getName(), currentFlangeValue)).findFirst().get();

                double currentRatioValue = getRandom(ratio.get(currentSize.getName()));

                Gear gear = new Gear();
                gear.setName("NMRV");
                gear.setSize(currentSize);
                gear.setType(rndType);
                gear.setRatio(currentRatioValue);
                gear.setPam(currentPam);
                gear.setFlange(currentFlange);
                gearsSet.add(gear);
            } else {
                double currentPowerValue = getRandom(power.get(currentSize.getName()));
                Power currentPower = powersEngineList.stream().filter(n -> Objects.equals(n.getName(), currentPowerValue)).findFirst().get();

                int currentSpeedValue = getRandom(speed.get(currentSize.getName()));
                Speed currentSpeed = speedEngineList.stream().filter(n -> Objects.equals(n.getName(), currentSpeedValue)).findFirst().get();

                int currentFlangeValue = getRandom(engineFlangeParamList);
                Flange currentFlange = flangeEngineList.stream().filter(n -> Objects.equals(n.getName(), currentFlangeValue)).findFirst().get();

                Engine engine = new Engine();
                engine.setName(getRandom(engineNames));
                engine.setType(rndType);
                engine.setSize(currentSize);
                engine.setPower(currentPower);
                engine.setSpeed(currentSpeed);
                engine.setFlange(currentFlange);
                enginesSet.add(engine);
            }
        }

        gearRepo.saveAll(gearsSet);
        engineRepo.saveAll(enginesSet);

        int size = gearsSet.size() + enginesSet.size();
        for (int i = 1; i < size; i++) {
            Type rndType = getRandom(typeList);
            Address address = getRandom(addressesList.get(getRandom(addressesName)));

            AddressProduct addressProduct = new AddressProduct();
            addressProduct.setId((long) i);
            addressProduct.setProduct(rndType.getName().equals("gears") ? getRandomSet(gearsSet) : getRandomSet(enginesSet));
            addressProduct.setQty(ThreadLocalRandom.current().nextInt(10, 100));
            addressProduct.setAddress(address);
            addressProduct.setSpace(ThreadLocalRandom.current().nextInt(1, address.getCapacity() + 1));
            addressProductRepo.save(addressProduct);

            addressProducts.add(addressProduct);
        }

        for (int i = 0; i < addressProducts.size() / 2; i++) {
            Reserve reserve = new Reserve();
            reserve.setId((long) i);
            AddressProduct addressProduct = getRandom(addressProducts);
            reserve.setAddressProduct(addressProduct);
            reserve.setQty(ThreadLocalRandom.current().nextInt(1, addressProduct.getQty() / 2));
            reserve.setComment(getReserveComment());
            reserve.setUser(getRandom(users));
            reserveRepo.save(reserve);
        }

    }

    public static List<Size> getTypeSizeMap(List<Integer> sizeParamList, List<Size> sizesList, Type type, SizeTypeRepository sizeTypeRepo) {
        List<Size> sizes = new ArrayList<>();
        for (Integer i : sizeParamList) {
            Size size = sizesList.stream().filter(s -> Objects.equals(s.getName(), i)).findFirst().get();
            sizes.add(size);
            SizeType sizeType = new SizeType();
            sizeType.setType(type);
            sizeType.setSize(size);
            sizeTypeRepo.save(sizeType);
        }
        return sizes;
    }

    public static <T> T getRandomSet(Set<? extends T> items) {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, items.size());
        return items.stream().skip(randomIndex).findFirst().orElse(null);
    }


    public static <T> T getRandom(List<? extends T> items) {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, items.size());
        return items.get(randomIndex);
    }

    public static String getReserveComment() {
        return "для заказа №" + ThreadLocalRandom.current().nextInt(100, 1000);
    }

    public static void createUsers(UserService userService, RoleService roleService) {
        roleService.saveRole(new Role(null, "ROLE_USER"));
        roleService.saveRole(new Role(null, "ROLE_MANAGER"));
        roleService.saveRole(new Role(null, "ROLE_ADMIN"));

        userService.create(new User(null, "John", "Travolta", "john", "1234", generateRandomDate(), String.valueOf(ThreadLocalRandom.current().nextInt(1111111, 9999999)), Gender.MALE, new ArrayList<>(), null, null));
        userService.create(new User(null, "Will", "Smith", "will", "1234", generateRandomDate(), String.valueOf(ThreadLocalRandom.current().nextInt(1111111, 9999999)), Gender.MALE, new ArrayList<>(), null, null));
        userService.create(new User(null, "Jim", "Carry", "jim", "1234", generateRandomDate(), String.valueOf(ThreadLocalRandom.current().nextInt(1111111, 9999999)), Gender.MALE, new ArrayList<>(), null, null));
        userService.create(new User(null, "Arnold", "Schwarzenegger", "arnold", "1234", generateRandomDate(), String.valueOf(ThreadLocalRandom.current().nextInt(1111111, 9999999)), Gender.MALE, new ArrayList<>(), null, null));

        roleService.addRoleToUser("john", "ROLE_USER");
        roleService.addRoleToUser("will", "ROLE_MANAGER");
        roleService.addRoleToUser("jim", "ROLE_USER");
        roleService.addRoleToUser("jim", "ROLE_ADMIN");
        roleService.addRoleToUser("arnold", "ROLE_USER");
        roleService.addRoleToUser("arnold", "ROLE_ADMIN");
    }

    public static LocalDate generateRandomDate() {
        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2010, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

}
