package carsharing;

import carsharing.company.CompanyController;
import carsharing.configuration.DatabaseConfiguration;

public class Main {
    private static final String DATABASE_NAME_ARGUMENT = "-databaseFileName";
    private static final String DEFAULT_DATABASE_NAME = "testDb";

    public static void main(String[] args) {
        DatabaseConfiguration.launch(DatabaseConfiguration.class, getDatabaseName(args));
        CompanyController.launch();
    }

    private static String getDatabaseName(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (DATABASE_NAME_ARGUMENT.equals(args[i])) {
                if (++i < args.length) return args[i];
            }
        }
        return DEFAULT_DATABASE_NAME;
    }
}