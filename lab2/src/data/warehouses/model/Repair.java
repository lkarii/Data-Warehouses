package data.warehouses.model;

import data.warehouses.BulkRowConverter;
import data.warehouses.InsertRowConverter;

public class Repair implements BulkRowConverter, InsertRowConverter {
    private int id;

    private String date;

    private String description;

    private int totalCost;

    private int fkBreakdown;

    private int fkService;

    public Repair(int id, String date, String description, int totalCost, int fkBreakdown, int fkService) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.totalCost = totalCost;
        this.fkBreakdown = fkBreakdown;
        this.fkService = fkService;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public int getFkBreakdown() {
        return fkBreakdown;
    }

    public void setFkBreakdown(int fkBreakdown) {
        this.fkBreakdown = fkBreakdown;
    }

    public int getFkService() {
        return fkService;
    }

    public void setFkService(int fkService) {
        this.fkService = fkService;
    }

    @Override
    public String getObjectAsBulkRow() {
        return fkBreakdown + "|" + fkService + "|" + date + "|" + description + "|" + totalCost + "\n";
    }

    @Override
    public String getObjectAsInsertRow() {
        /*return "INSERT INTO Repair(repair_date, description, total_cost, fk_breakdown, fk_service) VALUES ('" +
                date + "', '" + description + "', '" + totalCost + "', '" + fkBreakdown + "', '" + fkService + "');";*/

        return "INSERT INTO Naprawa(fk_awaria, fk_serwis, data_naprawy, opis, koszt_laczny) VALUES ('" +
                fkBreakdown + "', '" + fkService + "', '" + date + "', '" + description + "', '" + totalCost + "');";
    }
}
