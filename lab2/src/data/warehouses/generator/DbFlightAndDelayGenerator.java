package data.warehouses.generator;

import data.warehouses.SourceReader;
import data.warehouses.model.*;
import data.warehouses.model.auxiliary.Route;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static data.warehouses.DateGenerationLogic.*;
import static data.warehouses.generator.DbBreakdownAndRepairGenerator.getLastBreakdown;
import static data.warehouses.generator.DbBreakdownAndRepairGenerator.getRepair;
import static data.warehouses.generator.DbPlaneGenerator.getOlderPlanes;

public class DbFlightAndDelayGenerator {
    private static List<Route> routes;

    private static List<String> causes;

    private static List<String> weatherReasons;

    private static List<String> otherReasons;

    private static List<String> incidentsOnBoard;

    private List<Flight> flights;

    private List<Flight> newFlights;

    private List<Delay> delays;

    private List<Delay> newDelays;

    public DbFlightAndDelayGenerator() {
        routes = null;

        flights = new ArrayList<>();
        newFlights = new ArrayList<>();
        delays = new ArrayList<>();
        newDelays = new ArrayList<>();
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public List<Flight> getNewFlights() {
        return newFlights;
    }

    public List<Delay> getDelays() {
        return delays;
    }

    public List<Delay> getNewDelays() {
        return newDelays;
    }

    public void loadDataSources() {
        routes = SourceReader.readRoutesFromCsv();

        causes = SourceReader.readDelayCauses();

        weatherReasons = SourceReader.readWeatherReasons();
        otherReasons = SourceReader.readOtherReasons();
        incidentsOnBoard = SourceReader.readIncidentsOnTheBoard();
    }

    public void generate(int[] input) {
        generateFlights(input[0]);
        generateNewFlights(input[1]);
        sortFlightsByArrivalDate();
        setCancelledFlights();

        generateDelays(input[2]);
        generateNewDelays(input[3]);
        sortDelaysByFkFlight();
    }

    private void generateFlights(int number) {
        addSerialFlights(number, false);
        addSingleFlights(number, false);
    }

    private void generateNewFlights(int number) {
        addSerialFlights(number, true);
        addSingleFlights(number, true);
    }

    private void addSerialFlights(int number, boolean newer) {
        List<Flight> flightList = newer ? newFlights : flights;

        for (int i = 0; i < number*0.8; ) {
            LocalDate date = designateDate(newer);
            List<Plane> planes = getOlderPlanes(date, newer);
            if (planes.size() != 0) {
                Route route = routes.get(ThreadLocalRandom.current().nextInt(0, routes.size()));

                LocalDateTime departureDate = getRandomDateWithHour(date);
                LocalDateTime arrivalDate = departureDate.plusMinutes(route.getMinutes());
                int fkPlane = planes.get(ThreadLocalRandom.current().nextInt(0, planes.size())).getId();

                Flight flight = new Flight(0, "", route.getFirstCity(), route.getSecondCity(),
                        getDateAsString(departureDate), getDateAsString(arrivalDate), 0, fkPlane);
                flightList.add(flight);
                ++i;

                addSubsequentFlights(number, newer, departureDate, arrivalDate, fkPlane, route);
            }
        }
    }

    private void addSubsequentFlights(int number, boolean newer, LocalDateTime departureDate, LocalDateTime arrivalDate,
                                      int fkPlane, Route route) {
        List<Flight> flightList = newer ? newFlights : flights;

        int temp = ThreadLocalRandom.current().nextInt(0, 25);
        for (int j = 0; j < temp && number < number*0.8; ++j) {
            departureDate = departureDate.plusWeeks(1);
            arrivalDate = arrivalDate.plusWeeks(1);

            Flight subsequentFlight = new Flight(0, "", route.getFirstCity(), route.getSecondCity(),
                    getDateAsString(departureDate), getDateAsString(arrivalDate), 0, fkPlane);
            flightList.add(subsequentFlight);
            ++number;
        }
    }

    private void addSingleFlights(int number, boolean newer) {
        List<Flight> flightList = newer ? newFlights : flights;

        for (int i = (int) (number*0.8); i < number;) {
            LocalDate date = designateDate(newer);
            List<Plane> planes = getOlderPlanes(date, newer);
            if (planes.size() != 0) {
                Route route = routes.get(ThreadLocalRandom.current().nextInt(0, routes.size()));

                LocalDateTime departureDate = getRandomDateWithHour(date);
                LocalDateTime arrivalDate = departureDate.plusMinutes(route.getMinutes());
                int fkPlane = planes.get(ThreadLocalRandom.current().nextInt(0, planes.size())).getId();

                Flight flight = new Flight(0, "", route.getFirstCity(), route.getSecondCity(),
                        getDateAsString(departureDate), getDateAsString(arrivalDate), 0, fkPlane);
                flightList.add(flight);

                ++i;
            }
        }
    }

    private LocalDate designateDate(boolean newer) {
        return newer ? getRandomDate(2017, 2020) : getPartiallyRandomDate();
    }

    private void sortFlightsByArrivalDate() {
        int id = 0;
        flights.sort(Comparator.comparing(Flight::getArrivalDate));
        for (Flight flight : flights) {
            flight.setId(++id);
            flight.setFlightNumber("PA/" + id);
        }

        newFlights.sort(Comparator.comparing(Flight::getArrivalDate));
        for (Flight flight : newFlights) {
            flight.setId(++id);
            flight.setFlightNumber("PA/" + id);
        }
    }

    private void setCancelledFlights() {
        for (int i = 0; i < flights.size() / 10000; ++i) {
            flights.get(ThreadLocalRandom.current().nextInt(0, flights.size())).setCanceled(1);
        }

        for (int j = 0; j < newFlights.size() / 10000; ++j) {
            newFlights.get(ThreadLocalRandom.current().nextInt(0, newFlights.size())).setCanceled(1);
        }
    }

    private void generateDelays(int number) {
        List<Integer> flightIds = getFlightIds(false);

        for (int i = 0; i < number; ++i) {
            int minutes = ThreadLocalRandom.current().nextInt(0, 1200);
            String cause = drawCause();

            int index = ThreadLocalRandom.current().nextInt(0, flightIds.size());
            int fkFlight = flightIds.get(index);
            flightIds.remove(index);

            Flight flight = flights.get(fkFlight - 1);
            flight.setDepartureDate(getNewDate(flight.getDepartureDate(), minutes));
            flight.setArrivalDate(getNewDate(flight.getArrivalDate(), minutes));

            Integer fkBreakdown = null;
            if (cause.equals( "awaria samolotu")) {
                int fkPlane = flight.getFkPlane();
                Breakdown breakdown = getLastBreakdown(fkPlane, flight.getDepartureDate(), false);

                if (breakdown != null && breakdown.getStatus().equals("repaired")) {
                    int breakdownId = breakdown.getId();
                    Repair repair = getRepair(breakdownId, false);

                    if (repair != null) {
                        breakdown.setDate(getPreviousDate(flight.getDepartureDate()));
                        repair.setDate(getNextDate(breakdown.getDate(), minutes));
                        fkBreakdown = breakdownId;
                    }
                    else {
                        --i;
                        continue;
                    }
                }
                else {
                    --i;
                    continue;
                }
            }

            Delay delay = new Delay(0, minutes, cause, getCauseDescription(cause), fkFlight, fkBreakdown);
            delays.add(delay);
        }
    }

    private void generateNewDelays(int number) {
        List<Integer> flightIds = getFlightIds(true);

        for (int i = 0; i < number; ++i) {
            int minutes = ThreadLocalRandom.current().nextInt(0, 1200);
            String cause = drawCause();

            int index = ThreadLocalRandom.current().nextInt(0, flightIds.size());
            int fkFlight = flightIds.get(index);
            flightIds.remove(index);

            Flight flight = newFlights.get(fkFlight - flights.size() - 1);
            flight.setDepartureDate(getNewDate(flight.getDepartureDate(), minutes));
            flight.setArrivalDate(getNewDate(flight.getArrivalDate(), minutes));

            Integer fkBreakdown = null;
            if (cause.equals( "awaria samolotu")) {
                int fkPlane = flight.getFkPlane();
                Breakdown breakdown = getLastBreakdown(fkPlane, flight.getDepartureDate(), true);

                if (breakdown != null && breakdown.getStatus().equals("repaired")) {
                    int breakdownId = breakdown.getId();
                    Repair repair = getRepair(breakdownId, true);

                    if (repair != null) {
                        breakdown.setDate(getPreviousDate(flight.getDepartureDate()));
                        repair.setDate(getNextDate(breakdown.getDate(), minutes));
                        fkBreakdown = breakdownId;
                    }
                    else {
                        --i;
                        continue;
                    }
                }
            }

            Delay delay = new Delay(0, minutes, cause, getCauseDescription(cause), fkFlight, fkBreakdown);
            newDelays.add(delay);
        }
    }

    private List<Integer> getFlightIds(boolean newer) {
        List<Flight> flightList = newer ? newFlights : flights;

        return flightList.stream()
                .map(Flight::getId)
                .collect(Collectors.toList());
    }

    private static String drawCause() {
        int temp = ThreadLocalRandom.current().nextInt(0, 100);

        if (temp < 30)
            return causes.get(0);
        else if (temp < 39)
            return causes.get(1);
        else if (temp < 65)
            return causes.get(2);
        else
            return causes.get(3);
    }

    private static String getNewDate(String date, int delay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        dateTime = dateTime.plusMinutes(delay);

        StringBuilder result = new StringBuilder(dateTime.toString());
        result.setCharAt(10, ' ');
        result.append(":00");
        return result.toString();
    }

    private static String getPreviousDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        dateTime = dateTime.minusMinutes(ThreadLocalRandom.current().nextInt(0, 300));

        StringBuilder result = new StringBuilder(dateTime.toString());
        result.setCharAt(10, ' ');
        result.append(":00");
        return result.toString();
    }

    private static String getNextDate(String date, int delay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        dateTime = dateTime.plusMinutes(delay - ThreadLocalRandom.current().nextInt(0, 35));

        StringBuilder result = new StringBuilder(dateTime.toString());
        result.setCharAt(10, ' ');
        result.append(":00");
        return result.toString();
    }

    private static String getCauseDescription(String cause) {
        return switch (cause) {
            case "zle warunki pogodowe" -> weatherReasons.get(ThreadLocalRandom.current().nextInt(0, weatherReasons.size()));
            case "inne" -> otherReasons.get(ThreadLocalRandom.current().nextInt(0, otherReasons.size()));
            case "incydent na pokladzie" -> incidentsOnBoard.get(ThreadLocalRandom.current().nextInt(0, incidentsOnBoard.size()));
            default -> "Sprawdź raport awarii";
        };
    }

    private void sortDelaysByFkFlight() {
        int id = 0;
        delays.sort(Comparator.comparing(Delay::getFkFlight));
        for (Delay delay : delays) {
            delay.setId(++id);
        }

        newDelays.sort(Comparator.comparing(Delay::getFkFlight));
        for (Delay delay : newDelays) {
            delay.setId(++id);
        }
    }
}
