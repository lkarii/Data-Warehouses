package data.warehouses.generator;

import data.warehouses.SourceReader;
import data.warehouses.model.AircraftService;

import java.util.ArrayList;
import java.util.List;

public class DbAircraftServiceGenerator {
    private List<AircraftService> sourceServices;

    private List<AircraftService> services;

    private List<AircraftService> newServices;

    public DbAircraftServiceGenerator() {
        sourceServices = null;
        services = null;
        newServices = null;
    }

    public List<AircraftService> getServices() {
        return services;
    }

    public List<AircraftService> getNewServices() {
        return newServices;
    }

    public void loadServicesFromCsv() {
        sourceServices = SourceReader.readServicesFromCsv();
    }

    public void generateAircraftServices(int aircraftServicesNumber, int newAircraftServicesNumber) {
        services = new ArrayList<>();
        for (int i = 0; i < aircraftServicesNumber; ++i) {
            services.add(sourceServices.get(i));
        }

        newServices = new ArrayList<>();
        for (int j = 0; j < newAircraftServicesNumber; ++j) {
            newServices.add(sourceServices.get(services.size() + j));
        }
    }
}
