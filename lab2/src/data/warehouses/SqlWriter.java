package data.warehouses;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;

public class SqlWriter {
    public static void writeDbPlanesToSql(String filename, LinkedHashMap<Integer, String> planes) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)))) {
            for (int i : planes.keySet()) {
                /*writer.write("INSERT INTO Plane(serial_number) VALUES ('" + planes.get(i) + "');");*/
                writer.write("INSERT INTO Samolot(numer_seryjny) VALUES ('" + planes.get(i) + "');");
                writer.write("\n");
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public static <T extends InsertRowConverter> void writeToSql(String filename, List<T> objects) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)))) {
            for (T object : objects) {
                writer.write(object.getObjectAsInsertRow());
                writer.write("\n");
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public static void writeUpdatesToSql(int breakdownId, int firstId, String firstDate, int secondId, String secondDate) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("generated_files/sql/Updates.sql")))) {
            /*writer.write("Update Breakdown SET status = 'repaired' WHERE id_breakdown = " + breakdownId + ";");
            writer.write("\n");

            writer.write("Update Flight SET arrival_date = '" + firstDate + "' WHERE id_flight = " + firstId + ";");
            writer.write("\n");
            writer.write("Update Flight SET arrival_date = '" + secondDate + "' WHERE id_flight = " + secondId + ";");
            writer.write("\n");

            writer.write("Update AircraftService SET description = 'Strategic partner in the field of aircraft repair' " +
                    "WHERE id_service = 2;");
            writer.write("\n");*/

            writer.write("Update Awaria SET status = 'repaired' WHERE id_awaria = " + breakdownId + ";");
            writer.write("\n");

            writer.write("Update Lot SET data_przylotu = '" + firstDate + "' WHERE id_lot = " + firstId + ";");
            writer.write("\n");
            writer.write("Update Lot SET data_przylotu = '" + secondDate + "' WHERE id_lot = " + secondId + ";");
            writer.write("\n");

            writer.write("Update SerwisSamolotowy SET opis = 'Partner strategiczny w zakresie naprawy samolotów' " +
                    "WHERE id_serwis = 2;");
            writer.write("\n");
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
