package carsharing.database;

import carsharing.company.Car;
import carsharing.company.Company;

import java.util.List;

public interface CarDAO extends DatabaseDAO<Car> {
    List<Car> getByCompanyId(int id);

    List<Car> getAvailableCarsByCompanyId(int id);

    Car findByName(String name);
}
