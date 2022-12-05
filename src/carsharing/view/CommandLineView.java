package carsharing.view;

import carsharing.company.Car;
import carsharing.company.Company;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommandLineView {
    private Scanner scanner = new Scanner(System.in);

    public int createView(List<String> options){
        printOptions(options);
        return getInput(options.size(), false);
    }

    public int createView(String title, List<String> options, String additionalOption){
        printTitle(title);
        printOptions(options);
        printAdditionalOption(additionalOption);
        return getInput(options.size(), true);
    }

    public <T> int createView(String title, List<T> options, Function<T, String> objectPrinter){
        printTitle(title);
        printOptions(options.stream().map(objectPrinter).collect(Collectors.toList()));
        return getInput(options.size(), true);
    }

    public <T> int createView(String title, List<T> options, String additionalOption, Function<T, String> objectPrinter){
        printTitle(title);
        printOptions(options.stream().map(objectPrinter).collect(Collectors.toList()));
        printAdditionalOption(additionalOption);
        return getInput(options.size(), true);
    }

    private void printTitle(String title){
        System.out.println();
        if(title != null) System.out.println(title);
    }
    private void printOptions(List<String> options){
        System.out.println();
        for(int i=0; i<options.size(); i++){
            System.out.printf("%d. %s%n", i+1, options.get(i));
        }
    }

    private void printAdditionalOption(String additionalOption){
        System.out.printf("0. %s%n", additionalOption);
    }

    public int getInput(int size, boolean hasAdditionalInput){
        prompt();
        String line = scanner.nextLine().trim();
        Set<String> validInputs = IntStream.rangeClosed(hasAdditionalInput?0:1, size).mapToObj(Integer::toString).collect(Collectors.toSet());
        while(!validInputs.contains(line)){
            System.out.println("Invalid input. Try again.");
            line = scanner.nextLine().trim();
        }
        return Integer.parseInt(line);
    }

    public String getNameInput(String prompt){
        System.out.println(prompt);
        prompt();
        String line = scanner.nextLine().trim();
        while(line.isBlank()){
            System.out.println("Invalid input. Try again.");
            line = scanner.nextLine().trim();
        }
        return line;
    }

    public <T> void showList(String title, List<T> list, Function<T, String> objectPrinter){
        printTitle(title);
        for(int i=0; i<list.size(); i++){
            System.out.printf("%d. %s%n", i+1, objectPrinter.apply(list.get(i)));
        }
    }

    public void showRentedCarInfo(Car car, Company company){
        System.out.println("Your rented car:");
        System.out.println(car.getName());
        System.out.println("Company:");
        System.out.println(company.getName());
    }

    public void showCreated(String row){
        System.out.printf("The %s was created!%n", row);
    }


    public void showNoCarsAvailable(Company company){
        System.out.printf("No available cars in the %s company%n", company.getName());
    }

    public void showSuccessfulRent(Car car){
        System.out.printf("You rented '%s'%n", car.getName());
    }

    public void showSuccessfulReturn(){
        System.out.println("You've returned a rented car!");
    }


    public void showAlreadyRenting(){
        System.out.println("You've already rented a car!");
    }
    public void showNoRentedCar(){
        System.out.println("You didn't rent a car!");
    }

    public void showEmptyList(String name){
        System.out.printf("The %s list is empty!%n", name);
    }

    public void prompt(){
        System.out.print("> ");
    }
}