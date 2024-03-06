package data.warehouses.model.auxiliary;

public class RepairDetail {
    private String description;

    private int basePrice;

    public RepairDetail(String description, int basePrice) {
        this.description = description;
        this.basePrice = basePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }
}
