package carsharing.database;

import carsharing.company.Customer;

public interface CustomerDAO extends DatabaseDAO<Customer>{
    public Customer getByName(String name);
}
