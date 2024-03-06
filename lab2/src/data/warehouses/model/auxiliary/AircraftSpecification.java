package data.warehouses.model.auxiliary;

public class AircraftSpecification {
    private String manufacturer;

    private String model;

    private int minSeats;

    private int maxSeats;

    private int startProductionYear;

    private int basePrice;

    private int baseWeight;

    public AircraftSpecification(String manufacturer, String model, int minSeats, int maxSeats, int startProductionYear,
                                 int basePrice, int baseWeight) {

        this.manufacturer = manufacturer;
        this.model = model;
        this.minSeats = minSeats;
        this.maxSeats = maxSeats;
        this.startProductionYear = startProductionYear;
        this.basePrice = basePrice;
        this.baseWeight = baseWeight;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMinSeats() {
        return minSeats;
    }

    public void setMinSeats(int minSeats) {
        this.minSeats = minSeats;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public int getStartProductionYear() {
        return startProductionYear;
    }

    public void setStartProductionYear(int startProductionYear) {
        this.startProductionYear = startProductionYear;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getBaseWeight() {
        return baseWeight;
    }

    public void setBaseWeight(int baseWeight) {
        this.baseWeight = baseWeight;
    }
}
