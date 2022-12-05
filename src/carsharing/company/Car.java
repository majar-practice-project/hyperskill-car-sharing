package carsharing.company;

public class Car {
    private Integer id;
    private String name;
    private Integer companyId;
    private boolean rented;

    public Car(String name, Integer companyId) {
        this.name = name;
        this.companyId = companyId;
    }

    public Car(Integer id, String name, Integer companyId, boolean rented) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.rented = rented;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public Integer getCompanyId() {
        return companyId;
    }
}
