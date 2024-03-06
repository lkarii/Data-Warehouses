package data.warehouses.model;

import data.warehouses.BulkRowConverter;
import data.warehouses.InsertRowConverter;

public class AircraftService implements BulkRowConverter, InsertRowConverter {
    private int id;

    private String serviceName;

    private String city;

    private String address;

    private String postalCode;

    private String description;

    public AircraftService(int id, String serviceName, String city, String address, String postalCode, String description) {
        this.id = id;
        this.serviceName = serviceName;
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getObjectAsBulkRow() {
        return serviceName + "|" + description + "|" + city + "|" + postalCode + "|" + address + "\n";
    }

    @Override
    public String getObjectAsInsertRow() {
        /*return "INSERT INTO AircraftService(service_name, city, address, postal_code, description) VALUES ('" + serviceName
                + "', '" + city + "', '" + address + "', '" + postalCode + "', '" + description + "');";*/
        return "INSERT INTO SerwisSamolotowy(nazwa_serwisu, opis, miasto, kod_pocztowy, adres) VALUES ('" + serviceName
                + "', '" + description + "', '" + city + "', '" + postalCode + "', '" + address + "');";
    }
}
