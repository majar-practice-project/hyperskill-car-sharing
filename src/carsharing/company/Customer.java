package carsharing.company;

public class Customer {
    private Integer id;
    private String name;
    private Integer rentedCarId;

    public Customer(String name) {
        this.name = name;
    }

    public Customer(String name, Integer rentedCarId) {
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public Customer(Integer id, String name, Integer rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public Customer(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}
