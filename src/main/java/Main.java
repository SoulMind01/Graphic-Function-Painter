package main.java;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Now running Main");
        Driver driver = new Driver();
        System.out.println("File name:");
        Scanner scanner = new Scanner(System.in);
        String file;
        if (scanner.hasNext()) {
            file = scanner.nextLine();
            driver.StartDriving(file);
        }
        System.out.println("Program finished");
        scanner.close();
    }
}
