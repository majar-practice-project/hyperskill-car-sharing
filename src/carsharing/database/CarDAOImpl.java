package carsharing.database;

import carsharing.company.Car;
import carsharing.configuration.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDAOImpl implements CarDAO {
    private static CarDAO instance;
    private Connection conn;

    private CarDAOImpl() {
        conn = DatabaseConfiguration.getConnection();
    }

    public static CarDAO getInstance() {
        if (instance == null) {
            instance = new CarDAOImpl();
        }
        return instance;
    }

    private static Car resultSetToCar(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String name = rs.getString("NAME");
        int companyId = rs.getInt("COMPANY_ID");
        boolean rented = rs.getBoolean("RENTED");
        return new Car(id, name, companyId, rented);
    }

    @Override
    public void update(Car car) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE CAR " +
                    "SET NAME = ?, COMPANY_ID = ?, RENTED = ? WHERE ID = ?;");
            statement.setString(1, car.getName());
            statement.setInt(2, car.getCompanyId());
            statement.setBoolean(3, car.isRented());
            statement.setInt(4, car.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Car> resultSetToList(ResultSet rs) {
        List<Car> cars = new ArrayList<>();
        try {
            while (rs.next()) {
                cars.add(resultSetToCar(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public List<Car> getAll() {
        String sql = "SELECT * FROM CAR ORDER BY ID;";
        try {
            ResultSet rs = conn.createStatement().executeQuery(sql);
            return resultSetToList(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car getById(int id) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM CAR WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            rs.next();
            return resultSetToCar(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> getByCompanyId(int id) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM CAR WHERE COMPANY_ID = ?");
            statement.setInt(1, id);
            return resultSetToList(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Car> getAvailableCarsByCompanyId(int id) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM CAR WHERE COMPANY_ID = ? AND RENTED = FALSE;");
            statement.setInt(1, id);
            return resultSetToList(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car findByName(String name) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM CAR WHERE NAME = ?");
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();

            rs.next();
            return resultSetToCar(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int add(Car car) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)");
            statement.setString(1, car.getName());
            statement.setInt(2, car.getCompanyId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
