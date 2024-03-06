package data.warehouses;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;

public class BulkWriter {
    public static void writeDbPlanesToBulk(String filename, LinkedHashMap<Integer, String> planes) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)))) {
            for (int i : planes.keySet()) {
                writer.write(planes.get(i) + "\n");
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public static <T extends BulkRowConverter> void writeToBulk(String filename, List<T> objects) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)))) {
            for (T object : objects) {
                writer.write(object.getObjectAsBulkRow());
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
