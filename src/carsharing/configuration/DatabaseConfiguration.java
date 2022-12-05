package carsharing.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfiguration {
    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL_REGEX = "jdbc:h2:./src/carsharing/db/%s";
    private static Connection conn;

    private DatabaseConfiguration(String newDbUrl) {
        if(conn == null){
            configure(newDbUrl);
        }
    }

    public static void launch(Class<? extends DatabaseConfiguration> config) {
        launch(config, "testDb");
    }

    public static void launch(Class<? extends DatabaseConfiguration> config, String databaseName) {
        final String DB_URL = String.format(DB_URL_REGEX, databaseName);
        new DatabaseConfiguration(DB_URL);
    }

    public void configure(String DB_URL) {
        Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
//            conn = DriverManager.getConnection(String.format(DB_URL, databaseName),USER,PASS);

            //STEP 3: Execute a query
            stmt = conn.createStatement();
            createCompanyTable(stmt);
            createCarTable(stmt);
            createCustomerTable(stmt);
            System.out.println("successful");
        } catch (Exception e) {
            //Handle errors for JDBC
            //Handle errors for Class.forName
            e.printStackTrace();
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            } // nothing we can do
        }
    }

    private void createCompanyTable(Statement statement) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                " NAME VARCHAR UNIQUE NOT NULL)";
        statement.executeUpdate(sql);
    }

    private void createCarTable(Statement statement) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS CAR (" +
                "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "NAME VARCHAR UNIQUE NOT NULL," +
                "COMPANY_ID INT NOT NULL, " +
                "RENTED BOOLEAN DEFAULT FALSE," +
                "CONSTRAINT FK_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID));";
        statement.executeUpdate(sql);
    }

    private void createCustomerTable(Statement statement) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                "ID INT PRIMARY KEY AUTO_INCREMENT," +
                "NAME VARCHAR UNIQUE NOT NULL," +
                "RENTED_CAR_ID INT, " +
                "CONSTRAINT FK_RENTED_CAR FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID));";
        statement.executeUpdate(sql);
    }

    public static Connection getConnection() {
        if(conn == null){
            throw new RuntimeException("Database not configured before access!");
        }
        return conn;
    }
}
