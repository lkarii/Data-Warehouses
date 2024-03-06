package data.warehouses.generator;

import data.warehouses.SourceReader;
import data.warehouses.model.AircraftService;
import data.warehouses.model.Plane;
import data.warehouses.model.Breakdown;
import data.warehouses.model.Repair;
import data.warehouses.model.auxiliary.RepairDetail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static data.warehouses.DateGenerationLogic.*;
import static data.warehouses.generator.DbPlaneGenerator.getOlderPlanes;

public class DbBreakdownAndRepairGenerator {
    private static List<String> causes;

    private static List<RepairDetail> repairDetails;

    private static List<Breakdown> breakdowns;

    private static List<Breakdown> newBreakdowns;

    private static List<Repair> repairs;

    private static List<Repair> newRepairs;

    public DbBreakdownAndRepairGenerator() {
        causes = null;
        repairDetails = null;

        breakdowns = new ArrayList<>();
        newBreakdowns = new ArrayList<>();
        repairs = new ArrayList<>();
        newRepairs = new ArrayList<>();

    }

    public List<Breakdown> getBreakdowns() {
        return breakdowns;
    }

    public List<Breakdown> getNewBreakdowns() {
        return newBreakdowns;
    }

    public List<Repair> getRepairs() {
        return repairs;
    }

    public List<Repair> getNewRepairs() {
        return newRepairs;
    }

    public void loadDataSourcesFromCsv() {
        causes = SourceReader.readBreakdownCauses();
        repairDetails = SourceReader.readRepairDetailsFromCsv();
    }

    public static Breakdown getLastBreakdown(int planeId, String date, boolean newer) {
        List<Breakdown> breakdownList = newer ? newBreakdowns : breakdowns;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        List<Breakdown> planeBreakdowns = breakdownList.stream()
                .filter(b -> b.getFkPlane() == planeId)
                .sorted(Comparator.comparing(Breakdown::getDate))
                .collect(Collectors.toList());

        for (Breakdown breakdown : planeBreakdowns) {
            LocalDateTime ldt = LocalDateTime.parse(breakdown.getDate(), formatter);
            if (ldt.isAfter(dateTime))
                return breakdown;
        }

        return null;
    }

    public static Repair getRepair(int breakdownId, boolean newer) {
        List<Repair> repairList = newer ? newRepairs : repairs;

        for (Repair repair : repairList) {
            if (repair.getFkBreakdown() == breakdownId)
                return repair;
        }

        return null;
    }

    public void generate(int[] input, List<AircraftService> services, List<AircraftService> newServices) {
        generateBreakdowns(input[0], false);
        generateBreakdowns(input[1], true);
        sortBreakdownsByDate();

        generateRepairs(input[2], services);
        generateNewRepairs(input[3], services, newServices);
        sortRepairsByDate();
    }

    private void generateBreakdowns(int number, boolean newer) {
        for (int i = 0; i < number; ++i) {
            LocalDate date = newer ? getRandomDate(2017, 2020) : getRandomDate(1978, 2016);
            LocalDateTime ldt = getRandomDateWithHour(date);

            List<Plane> planes = getOlderPlanes(date, false);
            if (planes.size() != 0)
                addBreakdown(planes, ldt, newer);
            else
                --i;
        }
    }

    private void addBreakdown(List<Plane> planes, LocalDateTime ldt, boolean newer) {
        int index = ThreadLocalRandom.current().nextInt(0, planes.size());
        int fkId = planes.get(index).getId();

        int causeIndex = ThreadLocalRandom.current().nextInt(0, causes.size() - 1);
        String cause = causes.get(causeIndex);

        Breakdown breakdown = new Breakdown(0, getDateAsString(ldt), "unrepaired", cause, fkId, causeIndex);
        if (newer)
            newBreakdowns.add(breakdown);
        else
            breakdowns.add(breakdown);
    }

    private void sortBreakdownsByDate() {
        int id = 0;
        breakdowns.sort(Comparator.comparing(Breakdown::getDate));
        for (Breakdown breakdown : breakdowns) {
            breakdown.setId(++id);
        }

        newBreakdowns.sort(Comparator.comparing(Breakdown::getDate));
        for (Breakdown newBreakdown : newBreakdowns) {
            newBreakdown.setId(++id);
        }
    }

    private void generateRepairs(int number, List<AircraftService> services) {
        List<Integer> breakdownIds = getBreakdownIds(false);

        for (int i = 0; i < number; ++i) {
            addRepair(breakdownIds, services, false, false);
        }
    }

    private void generateNewRepairs(int number, List<AircraftService> services, List<AircraftService> newServices) {
        List<Integer> breakdownIds = getBreakdownIds(true);
        List<Integer> unrepairedBreakdownIds = getUnrepairedBreakdownIds();

        List<AircraftService> serviceList = new ArrayList<>(services);
        serviceList.addAll(newServices);

        for (int i = 0; i < number; ++i) {
            int aux = ThreadLocalRandom.current().nextInt(0, 100);

            if (aux < 20 && unrepairedBreakdownIds.size() > 5)
                addRepair(unrepairedBreakdownIds, serviceList, true, false);
            else
                addRepair(breakdownIds, serviceList, true, true);
        }
    }

    private void addRepair(List<Integer> breakdownIds, List<AircraftService> services, boolean newer, boolean shiftedIndex) {
        List<Repair> repairList = newer ? newRepairs : repairs;

        int temp = ThreadLocalRandom.current().nextInt(0, breakdownIds.size() - 1);
        int index = breakdownIds.get(temp);
        breakdownIds.remove(temp);

        Breakdown breakdown = (newer && shiftedIndex) ?
                newBreakdowns.get(index - breakdowns.size()) : breakdowns.get(index - 1);
        breakdown.setStatus("repaired");

        int causeIndex = breakdown.getCauseIndex();

        int serviceIndex = ThreadLocalRandom.current().nextInt(0, services.size() - 1);
        int fkService = services.get(serviceIndex).getId();

        int cost = computeCost(repairDetails.get(causeIndex).getBasePrice(), fkService);
        String date = getRandomFutureDateAsString(breakdown.getDate(), newer, shiftedIndex);

        Repair repair = new Repair(0, date, repairDetails.get(causeIndex).getDescription(), cost,
                breakdown.getId(), fkService);

        repairList.add(repair);
    }

    private List<Integer> getBreakdownIds(boolean newer) {
        List<Breakdown> breakdownList = newer ? newBreakdowns : breakdowns;

        return breakdownList.stream()
                .map(Breakdown::getId)
                .collect(Collectors.toList());
    }

    private List<Integer> getUnrepairedBreakdownIds() {
        return breakdowns.stream()
                .filter(b -> b.getStatus().equals("unrepaired"))
                .map(Breakdown::getId)
                .collect(Collectors.toList());
    }

    private int computeCost(int basePrice, int fkService) {
        int additionalCost = 0;
        int difference = fkService % 25;

        if (difference < 6)
            additionalCost = 100*ThreadLocalRandom.current().nextInt(0, 500);

        if (difference == 1)
            additionalCost += 100000;
        else if (difference == 4)
            additionalCost += 50000;

        if (difference > 8 && difference < 24)
            additionalCost += (difference * 1000);

        return basePrice + additionalCost;
    }

    private void sortRepairsByDate() {
        int id = 0;
        repairs.sort(Comparator.comparing(Repair::getDate));
        for (Repair repair : repairs) {
            repair.setId(++id);
        }

        newRepairs.sort(Comparator.comparing(Repair::getDate));
        for (Repair repair : newRepairs) {
            repair.setId(++id);
        }
    }
}
