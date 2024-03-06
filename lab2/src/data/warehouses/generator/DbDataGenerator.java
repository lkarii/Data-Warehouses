package data.warehouses.generator;

import data.warehouses.model.Breakdown;
import data.warehouses.model.Flight;
import data.warehouses.model.Plane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static data.warehouses.BulkWriter.*;
import static data.warehouses.BulkWriter.writeToBulk;
import static data.warehouses.SqlWriter.*;

public class DbDataGenerator {
    private final String BULK_DIRECTORY = "generated_files/bulk/";

    private final String SQL_DIRECTORY = "generated_files/sql/";

    private DbPlaneGenerator dbPlaneGenerator;

    private DbBreakdownAndRepairGenerator dbBreakdownAndRepairGenerator;

    private DbAircraftServiceGenerator dbAircraftServiceGenerator;

    private DbFlightAndDelayGenerator dbFlightAndDelayGenerator;

    public DbDataGenerator() {
        dbPlaneGenerator = new DbPlaneGenerator();
        dbBreakdownAndRepairGenerator = new DbBreakdownAndRepairGenerator();
        dbAircraftServiceGenerator = new DbAircraftServiceGenerator();
        dbFlightAndDelayGenerator = new DbFlightAndDelayGenerator();

        preloadSourceData();
    }

    public void loadSourceData(List<Plane> planes, List<Plane> updatedPlanes) {
        dbPlaneGenerator.loadSourcePlanes(planes, updatedPlanes);
    }

    public void generateAndSaveData(int[] userInput, boolean update) {
        dbPlaneGenerator.generateDbPlanes();

        dbAircraftServiceGenerator.generateAircraftServices(userInput[0], userInput[1]);

        dbBreakdownAndRepairGenerator.generate(filterInput(userInput, true), dbAircraftServiceGenerator.getServices(),
                dbAircraftServiceGenerator.getNewServices());

        dbFlightAndDelayGenerator.generate(filterInput(userInput, false));

        writeToBulks();
        writeToSqls();

        if (update)
            writeUpdateToSql();
    }

    private void writeToBulks() {
        writeDbPlanesToBulk(BULK_DIRECTORY + "Samoloty_t1.bulk", dbPlaneGenerator.getDbPlanes());
        writeDbPlanesToBulk(BULK_DIRECTORY + "Samoloty_t2.bulk", dbPlaneGenerator.getNewDbPlanes());

        writeToBulk(BULK_DIRECTORY + "SerwisSamolotowy_t1.bulk", dbAircraftServiceGenerator.getServices());
        writeToBulk(BULK_DIRECTORY + "SerwisSamolotowy_t2.bulk", dbAircraftServiceGenerator.getNewServices());

        writeToBulk(BULK_DIRECTORY + "Awarie_t1.bulk", dbBreakdownAndRepairGenerator.getBreakdowns());
        writeToBulk(BULK_DIRECTORY + "Awarie_t2.bulk", dbBreakdownAndRepairGenerator.getNewBreakdowns());

        writeToBulk(BULK_DIRECTORY + "Naprawy_t1.bulk", dbBreakdownAndRepairGenerator.getRepairs());
        writeToBulk(BULK_DIRECTORY + "Naprawy_t2.bulk", dbBreakdownAndRepairGenerator.getNewRepairs());

        writeToBulk(BULK_DIRECTORY + "Loty_t1.bulk", dbFlightAndDelayGenerator.getFlights());
        writeToBulk(BULK_DIRECTORY + "Loty_t2.bulk", dbFlightAndDelayGenerator.getNewFlights());

        writeToBulk(BULK_DIRECTORY + "Opoznienia_t1.bulk", dbFlightAndDelayGenerator.getDelays());
        writeToBulk(BULK_DIRECTORY + "Opoznienia_t2.bulk", dbFlightAndDelayGenerator.getNewDelays());
    }

    private void writeToSqls() {
        writeDbPlanesToSql(SQL_DIRECTORY + "Samoloty_t1.sql", dbPlaneGenerator.getDbPlanes());
        writeDbPlanesToSql(SQL_DIRECTORY + "Samoloty_t2.sql", dbPlaneGenerator.getNewDbPlanes());

        writeToSql(SQL_DIRECTORY + "SerwisSamolotowy_t1.sql", dbAircraftServiceGenerator.getServices());
        writeToSql(SQL_DIRECTORY + "SerwisSamolotowy_t2.sql", dbAircraftServiceGenerator.getNewServices());

        writeToSql(SQL_DIRECTORY + "Awarie_t1.sql", dbBreakdownAndRepairGenerator.getBreakdowns());
        writeToSql(SQL_DIRECTORY + "Awarie_t2.sql", dbBreakdownAndRepairGenerator.getNewBreakdowns());

        writeToSql(SQL_DIRECTORY + "Naprawy_t1.sql", dbBreakdownAndRepairGenerator.getRepairs());
        writeToSql(SQL_DIRECTORY + "Naprawy_t2.sql", dbBreakdownAndRepairGenerator.getNewRepairs());

        writeToSql(SQL_DIRECTORY + "Loty_t1.sql", dbFlightAndDelayGenerator.getFlights());
        writeToSql(SQL_DIRECTORY + "Loty_t2.sql", dbFlightAndDelayGenerator.getNewFlights());

        writeToSql(SQL_DIRECTORY + "Opoznienia_t1.sql", dbFlightAndDelayGenerator.getDelays());
        writeToSql(SQL_DIRECTORY + "Opoznienia_t2.sql", dbFlightAndDelayGenerator.getNewDelays());
    }

    private void writeUpdateToSql() {
        int unrepairedBreakdownId = getUnrepairedBreakdownId();
        List<Flight> flights = dbFlightAndDelayGenerator.getFlights();
        Flight firstFlight = flights.get(ThreadLocalRandom.current().nextInt(0, flights.size()));
        String firstDate = getNewArrivalDate(firstFlight);
        Flight secondFlight = flights.get(ThreadLocalRandom.current().nextInt(0, flights.size()));
        String secondDate = getNewArrivalDate(secondFlight);

        writeUpdatesToSql(unrepairedBreakdownId, firstFlight.getId(), firstDate, secondFlight.getId(), secondDate);
    }

    private void preloadSourceData() {
        dbAircraftServiceGenerator.loadServicesFromCsv();
        dbBreakdownAndRepairGenerator.loadDataSourcesFromCsv();
        dbFlightAndDelayGenerator.loadDataSources();
    }

    private int[] filterInput(int[] userInput, boolean breakdownsAndRepairs) {
        if (breakdownsAndRepairs)
            return new int[] { userInput[2], userInput[3], userInput[4], userInput[5] };
        else
            return new int[] { userInput[6], userInput[7], userInput[8], userInput[9] };
    }

    private int getUnrepairedBreakdownId() {
        List<Breakdown> breakdowns = dbBreakdownAndRepairGenerator.getBreakdowns();
        List<Breakdown> unrepairedBreakdowns = breakdowns.stream()
                .filter(b -> b.getStatus().equals("unrepaired"))
                .collect(Collectors.toList());

        return unrepairedBreakdowns.get(ThreadLocalRandom.current().nextInt(0, unrepairedBreakdowns.size())).getId();
    }

    private String getNewArrivalDate(Flight flight) {
        String date = flight.getArrivalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        dateTime = dateTime.plusMinutes(55);

        StringBuilder result = new StringBuilder(dateTime.toString());
        result.setCharAt(10, ' ');
        result.append(":00");
        return result.toString();
    }
}
