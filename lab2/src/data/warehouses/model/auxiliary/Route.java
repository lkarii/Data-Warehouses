package data.warehouses.model.auxiliary;

public class Route {
    private String firstCity;

    private String secondCity;

    private int minutes;

    private int counter;

    public Route(String firstCity, String secondCity, int minutes) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.minutes = minutes;
        this.counter = 1;
    }

    public String getFirstCity() {
        return firstCity;
    }

    public void setFirstCity(String firstCity) {
        this.firstCity = firstCity;
    }

    public String getSecondCity() {
        return secondCity;
    }

    public void setSecondCity(String secondCity) {
        this.secondCity = secondCity;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void increaseCounter() {
        ++counter;
    }
}
