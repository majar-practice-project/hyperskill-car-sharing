package carsharing.database;

import carsharing.company.Car;
import carsharing.company.Company;
import carsharing.configuration.DatabaseConfiguration;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompanyDAOImpl implements CompanyDAO{
    private static CompanyDAO instance;
    private Connection conn = null;

    private CompanyDAOImpl() {
        conn = DatabaseConfiguration.getConnection();
    }
    @Override
    public List<Company> getAll() {
        String sql = "SELECT * FROM COMPANY ORDER BY ID;";
        try {
            ResultSet rs = conn.createStatement().executeQuery(sql);
            return resultSetToList(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int add(Company company) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO COMPANY (NAME) VALUES (?)");
            statement.setString(1, company.getName());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Company findByName(String name) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM COMPANY WHERE NAME=?;");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            rs.next();
            return resultSetToCompany(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String formatStatement(String regex, Object... args){
        return String.format(regex, Arrays.stream(args).map(arg -> arg instanceof String
                ? "'"+((String) arg).replaceAll("'", "&quot;")+"'"
                : arg).toArray());
    }

    @Override
    public Company getById(int id) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM COMPANY WHERE ID=?;");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            rs.next();
            return resultSetToCompany(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompanyDAO getInstance(){
        if(instance == null){
            instance = new CompanyDAOImpl();
        }
        return instance;
    }

    private static Company resultSetToCompany(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String name = rs.getString("NAME");
        return new Company(id, name);
    }

    private static List<Company> resultSetToList(ResultSet rs){
        List<Company> companies = new ArrayList<>();
        try {
            while (rs.next()) {
                companies.add(resultSetToCompany(rs));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public void update(Company company) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE TABLE COMPANY " +
                    "SET NAME = ? WHERE ID = ?;");
            statement.setString(1, company.getName());
            statement.setInt(2, company.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
