package carsharing.database;

import carsharing.company.Customer;
import carsharing.configuration.DatabaseConfiguration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    private static CustomerDAO instance;
    private Connection conn;

    public CustomerDAOImpl() {
        conn = DatabaseConfiguration.getConnection();
    }

    private static Customer resultSetToCustomer(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String name = rs.getString("NAME");
        int rentedCarId = rs.getInt("RENTED_CAR_ID");
        return rentedCarId == 0 ? new Customer(id, name) : new Customer(id, name, rentedCarId);
    }

    private static List<Customer> resultSetToList(ResultSet rs) {
        List<Customer> customers = new ArrayList<>();
        try {
            while (rs.next()) {
                customers.add(resultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public static CustomerDAO getInstance() {
        if (instance == null) instance = new CustomerDAOImpl();
        return instance;
    }

    @Override
    public List<Customer> getAll() {
        String sql = "SELECT * FROM CUSTOMER;";
        try {
            ResultSet rs = conn.createStatement().executeQuery(sql);
            return resultSetToList(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int add(Customer customer) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO CUSTOMER (NAME) VALUES (?)");
            statement.setString(1, customer.getName());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Customer customer) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE CUSTOMER " +
                    "SET NAME = ?, RENTED_CAR_ID = ? WHERE ID = ?;");
            statement.setString(1, customer.getName());
            if (customer.getRentedCarId() == null) {
                statement.setNull(2, Types.INTEGER);
            } else {
                statement.setInt(2, customer.getRentedCarId());
            }
            statement.setInt(3, customer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer getById(int id) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM CUSTOMER WHERE ID = ?;");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            rs.next();
            return resultSetToCustomer(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer getByName(String name) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM CUSTOMER WHERE NAME = ?;");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            rs.next();
            return resultSetToCustomer(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
