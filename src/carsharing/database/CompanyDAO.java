package carsharing.database;

import carsharing.company.Company;

import java.util.List;

public interface CompanyDAO extends DatabaseDAO<Company>{
    Company findByName(String name);
}
