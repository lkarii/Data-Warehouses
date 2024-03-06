package data.warehouses.model;

import data.warehouses.BulkRowConverter;
import data.warehouses.InsertRowConverter;

public class Flight implements BulkRowConverter, InsertRowConverter {
    private int id;

    private String flightNumber;

    private String departureCity;

    private String arrivalCity;

    private String departureDate;

    private String arrivalDate;

    private int canceled; //0 or 1

    private int fkPlane;

    public Flight(int id, String flightNumber, String departureCity, String arrivalCity, String departureDate,
                  String arrivalDate, int canceled, int fkPlane) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.canceled = canceled;
        this.fkPlane = fkPlane;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getCanceled() {
        return canceled;
    }

    public void setCanceled(int canceled) {
        this.canceled = canceled;
    }

    public int getFkPlane() {
        return fkPlane;
    }

    public void setFkPlane(int fkPlane) {
        this.fkPlane = fkPlane;
    }

    @Override
    public String getObjectAsBulkRow() {
        return fkPlane + "|" + flightNumber + "|" + departureDate + "|" + arrivalDate + "|" + departureCity + "|"
                + arrivalCity + "|" + canceled + "\n";
    }

    @Override
    public String getObjectAsInsertRow() {
        /*return "INSERT INTO Flight(flight_number, departure_city, arrival_city, departure_date, arrival_date, " +
                "canceled, fk_plane) VALUES ('" + flightNumber + "', '" + departureCity + "', '" + arrivalCity + "', '"
                + departureDate + "', '" + arrivalDate + "', '" + canceled + "', '" + fkPlane + "');";*/

        return "INSERT INTO Lot(fk_samolot, numer_lotu, data_wylotu, data_przylotu, miasto_wylotu, " +
                "miasto_przylotu, odwolany) VALUES ('" + fkPlane + "', '" + flightNumber + "', '" + departureDate + "', '"
                + arrivalDate + "', '" + departureCity + "', '" + arrivalCity + "', '" + canceled + "');";
    }
}
