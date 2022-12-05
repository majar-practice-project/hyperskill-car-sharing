package carsharing.view;

import java.util.List;

public abstract class ViewResource {
    public final List<String> options;
    public final String additionalOption;
    public final String title;

    private ViewResource(String title, List<String> options, String additionalOption) {
        this.title = title;
        this.options = options;
        this.additionalOption = additionalOption;
    }

    public static ViewResource getHomePageResource(){
        return HomePageResource.getInstance();
    }

    public static ViewResource getDatabaseManagePageResource(){
        return DatabaseManagePageResource.getInstance();
    }

    public static ViewResource getCarManagePageResource(){
        return CarManagePageResource.getInstance();
    }

    public static ViewResource getCustomerManagePageResource(){
        return CustomerManagePageResource.getInstance();
    }

    public static class HomePageResource extends ViewResource{
        private static ViewResource instance;
        private HomePageResource() {
            super(null, List.of("Log in as a manager", "Log in as a customer", "Create a customer"),
                    "Exit");
        }

        public static ViewResource getInstance(){
            if(instance == null) instance = new HomePageResource();
            return instance;
        }
    }

    public static class DatabaseManagePageResource extends ViewResource{
        private static ViewResource instance;
        private DatabaseManagePageResource() {
            super(null, List.of("Company list", "Create a company"),
                    "Back");
        }

        public static ViewResource getInstance(){
            if(instance == null) instance = new DatabaseManagePageResource();
            return instance;
        }
    }

    public static class CarManagePageResource extends ViewResource{
        private static ViewResource instance;
        private CarManagePageResource() {
            super("'%s' company", List.of("Car list", "Create a car"),
                    "Back");
        }

        public static ViewResource getInstance(){
            if(instance == null) instance = new CarManagePageResource();
            return instance;
        }
    }

    public static class CustomerManagePageResource extends ViewResource{
        private static ViewResource instance;
        private CustomerManagePageResource() {
            super(null, List.of("Rent a car", "Return a rented car",
            "My rented car"), "Back");
        }

        public static ViewResource getInstance(){
            if(instance == null) instance = new CustomerManagePageResource();
            return instance;
        }
    }
}
