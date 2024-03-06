package data.warehouses;

import data.warehouses.model.AircraftService;
import data.warehouses.model.auxiliary.AircraftSpecification;
import data.warehouses.model.auxiliary.RepairDetail;
import data.warehouses.model.auxiliary.Route;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SourceReader {
    public static List<AircraftSpecification> readSpecificationsFromCsv() {
        List<AircraftSpecification> specifications = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(Paths.get("data_sources/models.csv"));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.getRecordNumber() != 1) {
                    AircraftSpecification as = new AircraftSpecification(csvRecord.get(0), csvRecord.get(1),
                            Integer.parseInt(csvRecord.get(2)), Integer.parseInt(csvRecord.get(3)),
                            Integer.parseInt(csvRecord.get(4)), Integer.parseInt(csvRecord.get(5)),
                            Integer.parseInt(csvRecord.get(6)));

                    specifications.add(as);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return specifications;
    }

    public static List<String> readBreakdownCauses() {
        List<String> breakdownCauses = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(Paths.get("data_sources/breakdown_causes.csv"));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.getRecordNumber() != 1)
                    breakdownCauses.add(csvRecord.get(0));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return breakdownCauses;
    }

    public static List<AircraftService> readServicesFromCsv() {
        List<AircraftService> services = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(Paths.get("data_sources/services_and_workshops.csv"));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            int id = 0;
            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.getRecordNumber() != 1) {
                    services.add(new AircraftService(++id, csvRecord.get(0), csvRecord.get(1), csvRecord.get(2),
                            csvRecord.get(3), csvRecord.get(4)));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return services;
    }

    public static List<RepairDetail> readRepairDetailsFromCsv() {
        List<RepairDetail> repairs = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(Paths.get("data_sources/repair_details.csv"));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.getRecordNumber() != 1) {
                    repairs.add(new RepairDetail(csvRecord.get(0), Integer.parseInt(csvRecord.get(1))));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return repairs;
    }

    public static List<Route> readRoutesFromCsv() {
        List<Route> routes = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(Paths.get("data_sources/routes.csv"));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.getRecordNumber() != 1) {
                    routes.add(new Route(csvRecord.get(0), csvRecord.get(1), Integer.parseInt(csvRecord.get(2))));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return routes;
    }

    public static List<String> readDelayCauses() {
        return new ArrayList<>(List.of( "zle warunki pogodowe", "awaria samolotu", "incydent na pokladzie", "inne"));
    }

    public static List<String> readWeatherReasons() {
        return new ArrayList<>(List.of("Mgla nad lotniskiem", "Silny wiatr w okolicy lotniska", "Geste chmury nad lotniskiem", "Huragan w okolicy lotniska", "Chmura popiolu wulkanicznego", "Burza na trasie", "Lod na pasie startowym", "Deszcz zagraza startowi na pasie", "Ciemnosc spowodowana zachmurzeniem", "Burza sniezna na trasie samolotu"));
    }

    public static List<String> readOtherReasons() {
        return new ArrayList<>(List.of("Pozar na lotnisku", "Oczekiwanie na inny lot", "Uszkodzony sprzet lotniskowy", "Spozniona zaloga lotnicza", "Brak paliwa", "Brak zgody od kontrolera", "Brak uprawnien zalogi", "Zagrozenie terrorystyczne na lotnisku docelowym", "Niebezpieczny bagaz", "Zagrozenie na lotnisku startowym", "Problemy prawne", "Zajety pas przez inny samolot", "Awaria oswietlenia na lotnisku", "Inne"));
    }

    public static List<String> readIncidentsOnTheBoard() {
        return new ArrayList<>(List.of("Agresywny pasazer", "Terrorysta na pokladzie", "Pijani pasazerowie", "Brakujacy pasazer/czlonek zalogi", "Problemy zdrowotne pasazera", "Podejrzany bagaz na pokladzie", "Pasazer bez biletu probowal poleciec", "Pasazerowie pod wplywem narkotykow na pokladzie", "Bijatyka na pokladzie", "Zniszczenie wyposazenia samolotu przez pasazera", "Inne"));
    }
}
