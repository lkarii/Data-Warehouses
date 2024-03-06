package data.warehouses.model;

import data.warehouses.BulkRowConverter;
import data.warehouses.InsertRowConverter;

public class Breakdown implements BulkRowConverter, InsertRowConverter {
    private int id;

    private String date;

    private String status;

    private String cause;

    private int fkPlane;

    private int causeIndex;

    public Breakdown(int id, String date, String status, String cause, int fkPlane, int causeIndex) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.cause = cause;
        this.fkPlane = fkPlane;
        this.causeIndex = causeIndex;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getFkPlane() {
        return fkPlane;
    }

    public void setFkPlane(int fkPlane) {
        this.fkPlane = fkPlane;
    }

    public int getCauseIndex() {
        return causeIndex;
    }

    public void setCauseIndex(int causeIndex) {
        this.causeIndex = causeIndex;
    }

    @Override
    public String getObjectAsBulkRow() {
        return fkPlane + "|" + date + "|" + cause + "|" + status + "\n";
    }

    @Override
    public String getObjectAsInsertRow() {
        /*return "INSERT INTO Breakdown(breakdown_date, status, cause, fk_plane) VALUES ('" + date + "', '"
                + status + "', '" + cause + "', '" + fkPlane + "');";*/
        return "INSERT INTO Awaria(fk_samolot, data_awarii, przyczyna, status) VALUES ('" + fkPlane + "', '"
                + date + "', '" + cause + "', '" + status + "');";

    }
}
