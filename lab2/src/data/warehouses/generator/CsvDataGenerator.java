package data.warehouses.generator;

import data.warehouses.CsvWriter;
import data.warehouses.SourceReader;
import data.warehouses.model.auxiliary.AircraftSpecification;
import data.warehouses.model.Plane;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static data.warehouses.DateGenerationLogic.getRandomDate;

public class CsvDataGenerator {
    private static List<Plane> planes;

    private static List<Plane> updatedPlanes;

    private static List<Integer> numbers;

    private static List<AircraftSpecification> aircraftSpecifications;

    private static String planeUpdateInfo;

    public CsvDataGenerator() {
        planes = null;
        updatedPlanes = null;
        numbers = null;
        planeUpdateInfo = "";
        aircraftSpecifications = SourceReader.readSpecificationsFromCsv();
    }

    public List<Plane> getPlanes() {
        return planes;
    }

    public List<Plane> getUpdatedPlanes() {
        return updatedPlanes;
    }

    public void savePlanesToCsvFiles(int planesNumber, int toUpdateNumber, int newPlanesNumber) {
        fillNumbers(10*(planesNumber + newPlanesNumber));

        generatePlanes(planesNumber);
        CsvWriter.writeToCsv(planes, "generated_files/csv/Samoloty_t1.csv");

        generateUpdatedPlanes(toUpdateNumber, newPlanesNumber);
        CsvWriter.writeToCsv(updatedPlanes, "generated_files/csv/Samoloty_t2.csv");
    }

    public String getPlaneUpdateInfo() {
        return planeUpdateInfo;
    }

    private void generatePlanes(int planesNumber) {
        planes = new ArrayList<>();
        for (int i = 0; i < planesNumber; ++i) {
            planes.add(generatePlane(false));
        }

        sortPlanesByPurchaseDate();
    }

    private void generateUpdatedPlanes(int toUpdateNumber, int newPlanesNumber) {
        updatedPlanes = new ArrayList<>(planes);
        performUpdate(toUpdateNumber);

        for (int i = 0; i < newPlanesNumber; ++i) {
            updatedPlanes.add(generatePlane(true));
        }

        sortUpdatedPlanesByPurchaseDate();
    }

    private static Plane generatePlane(boolean newer) {
        int index = ThreadLocalRandom.current().nextInt(0, aircraftSpecifications.size());
        String manufacturer = aircraftSpecifications.get(index).getManufacturer();
        String model = aircraftSpecifications.get(index).getModel();
        String serialNumber = createSerialNumber(manufacturer);
        LocalDate productionDate = getRandomDate(aircraftSpecifications.get(index).getStartProductionYear(), 2013);
        int seatsNumber = createSeatsNumber(index);
        int totalWeight = createTotalWeight(index, seatsNumber);
        int purchaseCost = createTotalCost(index, seatsNumber, productionDate.getYear());
        LocalDate purchaseDate = createPurchaseDate(productionDate, newer);

        return new Plane(0, serialNumber, manufacturer, model, productionDate,
                totalWeight, seatsNumber, purchaseCost, purchaseDate);
    }

    private void performUpdate(int toUpdateNumber) {
        for (int i = 0; i < toUpdateNumber; ++i) {
            int index = ThreadLocalRandom.current().nextInt(0, updatedPlanes.size());
            Plane plane = updatedPlanes.get(index);
            plane.setSeatsNumber(plane.getSeatsNumber() + 10);
            plane.setTotalWeight(plane.getTotalWeight() + 500);
            updatedPlanes.set(index, plane);

            if (i == 0)
                planeUpdateInfo = "Plane " + plane.getSerialNumber() + " got 10 additional seats! <Row " + (index + 2) + ">\n";
        }
    }

    private static String createSerialNumber(String manufacturer) {
        char sign = (char) ThreadLocalRandom.current().nextInt('A', 'Z');
        int number = drawUniqueNumber();

        return String.valueOf(manufacturer.charAt(0)) + sign + number;
    }

    private static int createSeatsNumber(int index) {
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 4);

        if (randomNumber == 1)
            return aircraftSpecifications.get(index).getMinSeats();
        else
            return randomNumber == 2 ? aircraftSpecifications.get(index).getMaxSeats() :
                    aircraftSpecifications.get(index).getMinSeats() + 5;
    }

    private static int createTotalWeight(int index, int seatsNumber) {
        return aircraftSpecifications.get(index).getBaseWeight()*1_000 + seatsNumber*50;
    }

    private static int createTotalCost(int index, int seatsNumber, int productionYear) {
        int basePrice = aircraftSpecifications.get(index).getBasePrice() * 1_000_000;
        int additionalCost = ThreadLocalRandom.current().nextInt(1, 10) * 200_000;
        int seatsCost = seatsNumber * 1_000;
        int ageReduction = (2021 - productionYear) * 100_000;

        return basePrice + additionalCost + seatsCost - ageReduction;
    }

    private static LocalDate createPurchaseDate(LocalDate productionDate, boolean newer) {
        return newer ? getRandomDate(2017, 2019): getRandomDate(productionDate.getYear() + 1, productionDate.getYear() + 3);
    }

    private static int drawUniqueNumber() {
        int index = ThreadLocalRandom.current().nextInt(0, numbers.size());
        int number = numbers.get(index);
        numbers.remove(index);

        return number;
    }

    private static void fillNumbers(int range) {
        numbers = IntStream.rangeClosed(100, range)
                .boxed()
                .collect(Collectors.toList());
    }

    private void sortPlanesByPurchaseDate() {
        planes.sort(Comparator.comparing(Plane::getPurchaseDate));

        int id = 0;
        for (Plane plane : planes) {
            plane.setId(++id);
        }
    }

    private void sortUpdatedPlanesByPurchaseDate() {
        updatedPlanes.sort(Comparator.comparing(Plane::getPurchaseDate));

        int id = 0;
        for (Plane plane : updatedPlanes) {
            plane.setId(++id);
        }
    }
}
