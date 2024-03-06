package data.warehouses;

import data.warehouses.model.Plane;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter {
    /*private static final String[] HEADERS = {"serial_number", "manufacturer", "model", "production_date",
            "total_weight", "seats_number", "purchase_cost", "purchase_date"};*/
    private static final String[] HEADERS = {"model", "numer_seryjny", "liczba_miejsc", "producent",
            "data_produkcji", "data_zakupu", "koszt_zakupu", "calkowita_waga"};

    public static void writeToCsv(List<Plane> planes, String filename) {
        CSVFormat csvFormat = CSVFormat.Builder.create().setHeader(HEADERS).build();

        try (FileWriter fileWriter = new FileWriter(filename);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFormat)) {
            for (Plane plane : planes) {
                //shit code, but CSVFormat always adds additional " to start and end of the row, this is solution for this problem
                /*csvPrinter.printRecord(plane.getSerialNumber(), plane.getManufacturer(), plane.getModel(),
                        plane.getProductionDate().toString(), Integer.toString(plane.getTotalWeight()),
                        Integer.toString(plane.getSeatsNumber()), Integer.toString(plane.getPurchaseCost()),
                        plane.getPurchaseDate().toString());*/
                csvPrinter.printRecord(plane.getModel(), plane.getSerialNumber(), Integer.toString(plane.getSeatsNumber()),
                        plane.getManufacturer(), plane.getProductionDate().toString(), plane.getPurchaseDate().toString(),
                        Integer.toString(plane.getPurchaseCost()), Integer.toString(plane.getTotalWeight()));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
