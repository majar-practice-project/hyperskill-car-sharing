package carsharing.company;

import carsharing.configuration.DatabaseConfiguration;
import carsharing.database.*;
import carsharing.view.CommandLineView;
import carsharing.view.StackNavigator;
import carsharing.view.StackNavigatorImpl;
import carsharing.view.ViewResource;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class CompanyController {
    private final CommandLineView view = new CommandLineView();
    private final StackNavigator navigator = new StackNavigatorImpl();
    private final CompanyDAO companyDAO = CompanyDAOImpl.getInstance();
    private final CarDAO carDAO = CarDAOImpl.getInstance();
    private final CustomerDAO customerDAO = CustomerDAOImpl.getInstance();

    private CompanyController(){}
    public void navigateToHomePage(){
        ViewResource resource = ViewResource.getHomePageResource();
        int output = view.createView(resource.title, resource.options, resource.additionalOption);
        switch (output) {
            case 0 -> exit();
            case 1 -> navigator.navigate(this::navigateToDatabaseManagePage);
            case 2 -> navigator.navigate(this::navigateToCustomerSelectionPage);
            case 3 -> {
                addCustomer();
                navigator.stay();
            }
        }
    }

    private void exit(){
        try {
            DatabaseConfiguration.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void navigateToDatabaseManagePage(){
        ViewResource resource = ViewResource.getDatabaseManagePageResource();
        int output = view.createView(resource.title, resource.options, resource.additionalOption);
        switch (output) {
            case 0 -> navigator.back();
            case 1 -> navigator.navigate(()->navigateToCompanySelectionPage(company->navigator.navigate(()->navigateToCarManagePage(company))));
            case 2 -> {
                addCompany();
                navigator.stay();
            }
            default -> throw new RuntimeException("Shouldn't happen");
        }
    }

    public void navigateToCompanySelectionPage(Consumer<Company> action){
        List<Company> companies = companyDAO.getAll();
        if(companies.isEmpty()){
            view.showEmptyList("company");
            navigator.back();
        } else{
            int output = view.createView("Choose a company:", companies, "Back", Company::getName);
            if (output == 0) {
                navigator.back();
            } else {
                action.accept(companyDAO.findByName(companies.get(output - 1).getName()));
            }
        }
    }

    public void navigateToCustomerSelectionPage(){
        List<Customer> customers = customerDAO.getAll();
        if(customers.isEmpty()){
            view.showEmptyList("customer");
            navigator.back();
        } else{
            int output = view.createView("Customer list:", customers, "Back", Customer::getName);
            if (output == 0) {
                navigator.back();
            } else {
                Customer customer = customerDAO.getByName(customers.get(output-1).getName());
                navigator.navigate(()->navigateToRentedCarManagePage(customer));
            }
        }
    }

    public void navigateToCarSelectionPage(Company company, Consumer<Car> selectedAction){
        List<Car> cars = carDAO.getAvailableCarsByCompanyId(company.getId());
        if(cars.isEmpty()){
            view.showNoCarsAvailable(company);
            navigator.back();
        } else{
            int output = view.createView("Choose a car:", cars, "Back", Car::getName);
            if (output == 0) {
                navigator.back();
            } else {
                selectedAction.accept(carDAO.findByName(cars.get(output - 1).getName()));
            }
        }
    }

    public void navigateToRentedCarManagePage(Customer customer){
        ViewResource resource = ViewResource.getCustomerManagePageResource();
        int output = view.createView(resource.title, resource.options, resource.additionalOption);
        switch (output) {
            case 1 -> rentCar(customer);
            case 2 -> returnCar(customer);
            case 3 -> displayRentedCar(customer);
            case 0 -> {
                navigator.back();
                return;
            }
        }
        navigator.stay();
    }


    public void navigateToCarManagePage(Company company){
        String companyName = company.getName();
        ViewResource resource = ViewResource.getCarManagePageResource();
        int output = view.createView(String.format(resource.title, companyName), resource.options, resource.additionalOption);
        switch (output) {
            case 1 -> {
                displayCars(company);
                navigator.stay();
            }
            case 2 -> {
                addCar(company);
                navigator.stay();
            }
            case 0 -> {
                navigator.drop();
                navigator.back();
            }
        }
    }

    private void addCompany(){
        String companyName = view.getNameInput("Enter the company name:");
        companyDAO.add(new Company(companyName));
        view.showCreated("company");
    }

    private void addCar(Company company){
        String name = view.getNameInput("Enter the car name:");
        carDAO.add(new Car(name, company.getId()));
        view.showCreated("car");
    }

    private void addCustomer(){
        String name = view.getNameInput("Enter the customer name:");
        customerDAO.add(new Customer(name));
        view.showCreated("customer");
    }

    private void displayCars(Company company){
        List<Car> cars = carDAO.getByCompanyId(company.getId());
        if(cars.isEmpty()){
            view.showEmptyList("car");
        } else{
            view.showList("Car list:", cars, Car::getName);
        }
    }

    private void displayRentedCar(Customer customer){
        if(customer.getRentedCarId() == null){
            view.showNoRentedCar();
        } else {
            Car rentedCar = carDAO.getById(customer.getRentedCarId());
            Company company = companyDAO.getById(rentedCar.getCompanyId());
            view.showRentedCarInfo(rentedCar, company);
        }
    }

    private Customer rentCar(Customer customer){
        if(customer.getRentedCarId() != null) {
            view.showAlreadyRenting();
            return customer;
        }
        navigator.navigate(()->navigateToCompanySelectionPage(company -> {
            navigator.navigate(()->navigateToCarSelectionPage(company, car -> {
                persistRentCar(customer, car);
                view.showSuccessfulRent(car);
                navigator.drop();
                navigator.back();
            }));
        }));

        return customer;
    }

    private Customer persistRentCar(Customer customer, Car car){
        customer.setRentedCarId(car.getId());
        car.setRented(true);
        customerDAO.update(customer);
        carDAO.update(car);
        return customer;
    }

    private Customer returnCar(Customer customer){
        if(customer.getRentedCarId() == null){
            view.showNoRentedCar();
            return customer;
        }
        customer = persistReturnCar(customer);
        view.showSuccessfulReturn();
        return customer;
    }

    private Customer persistReturnCar(Customer customer){
        Car rentedCar = carDAO.getById(customer.getRentedCarId());
        customer.setRentedCarId(null);
        rentedCar.setRented(false);
        customerDAO.update(customer);
        carDAO.update(rentedCar);
        return customer;
    }

    public static void launch(){
        CompanyController instance = new CompanyController();
        instance.navigator.navigate(instance::navigateToHomePage);
    }
}
