package test;

import main.java.Driver;

public class MainTest {
    public static void main(String[] args) {
        System.out.println("Running MainTest");
        Driver driver = new Driver();
        driver.StartDriving("testcases/draw.txt");
    }
}
