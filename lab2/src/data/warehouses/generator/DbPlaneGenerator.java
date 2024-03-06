package data.warehouses.generator;

import data.warehouses.model.Plane;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DbPlaneGenerator {
    private static List<Plane> planes;

    private static List<Plane> updatedPlanes;

    private LinkedHashMap<Integer, String> dbPlanes;

    private LinkedHashMap<Integer, String> newDbPlanes;

    public DbPlaneGenerator() {
        planes = null;
        updatedPlanes = null;
        dbPlanes = null;
        newDbPlanes = null;
    }

    public LinkedHashMap<Integer, String> getDbPlanes() {
        return dbPlanes;
    }

    public LinkedHashMap<Integer, String> getNewDbPlanes() {
        return newDbPlanes;
    }

    public void loadSourcePlanes(List<Plane> planes, List<Plane> updatedPlanes) {
        DbPlaneGenerator.planes = planes;
        DbPlaneGenerator.updatedPlanes = updatedPlanes;
    }

    public void generateDbPlanes() {
        dbPlanes = new LinkedHashMap<>();
        for (Plane plane : planes) {
            dbPlanes.put(plane.getId(), plane.getSerialNumber());
        }

        newDbPlanes = new LinkedHashMap<>();
        for (int i = planes.size() + 1; i <= updatedPlanes.size(); ++i) {
            newDbPlanes.put(updatedPlanes.get(i - 1).getId(), updatedPlanes.get(i - 1).getSerialNumber());
        }
    }

    public static List<Plane> getOlderPlanes(LocalDate date, boolean updated) {
        List<Plane> planeList = updated ? updatedPlanes : planes;

        return planeList.stream()
                .filter(p -> p.getPurchaseDate().compareTo(date) <= -1)
                .collect(Collectors.toList());
    }
}
