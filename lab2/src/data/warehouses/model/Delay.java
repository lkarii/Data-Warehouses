package data.warehouses.model;

import data.warehouses.BulkRowConverter;
import data.warehouses.InsertRowConverter;

public class Delay implements BulkRowConverter, InsertRowConverter {
    private int id;

    private int totalMinutes;

    private String cause;

    private String causeDescription;

    private int fkFlight;

    private Integer fkBreakdown;

    public Delay(int id, int totalMinutes, String cause, String causeDescription, int fkFlight, Integer fkBreakdown) {
        this.id = id;
        this.totalMinutes = totalMinutes;
        this.cause = cause;
        this.causeDescription = causeDescription;
        this.fkFlight = fkFlight;
        this.fkBreakdown = fkBreakdown;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getCauseDescription() {
        return causeDescription;
    }

    public void setCauseDescription(String causeDescription) {
        this.causeDescription = causeDescription;
    }

    public int getFkFlight() {
        return fkFlight;
    }

    public void setFkFlight(int fkFlight) {
        this.fkFlight = fkFlight;
    }

    public Integer getFkBreakdown() {
        return fkBreakdown;
    }

    public void setFkBreakdown(Integer fkBreakdown) {
        this.fkBreakdown = fkBreakdown;
    }

    @Override
    public String getObjectAsBulkRow() {
        return fkFlight + "|" + fkBreakdown + "|" + cause + "|" + causeDescription + "|" + totalMinutes + "\n";
    }

    @Override
    public String getObjectAsInsertRow() {
        /*String result = "INSERT INTO Delay(total_minutes, cause, cause_description, fk_flight, fk_breakdown) VALUES ('"
                + totalMinutes + "', '" + cause + "', '" + causeDescription + "', '" + fkFlight + "', ";

        if (fkBreakdown == null)
            result += "null);";
        else
            result += "'" + fkBreakdown + "');";

        return result;*/

        String result = "INSERT INTO Opoznienie(fk_lot, fk_awaria, przyczyna, opis_przyczyny, czas_opoznienia) VALUES ('"
                + fkFlight + "', ";

        if (fkBreakdown == null)
            result += "null" + ", '";
        else
            result += "'" + fkBreakdown + "', '";

        result += cause + "', '" + causeDescription + "', '" + totalMinutes + "');";
        return result;
    }
}
