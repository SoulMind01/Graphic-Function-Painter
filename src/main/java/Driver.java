package main.java;

import main.java.analyser.TokenScanner;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Driver {
    public void StartDriving(String File) {
        System.out.println("The program is running\n");
        TokenScanner tokenscanner = new TokenScanner();
        try {
            tokenscanner.init(File);
            System.out.println("Now scanning file " + File);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IO exception");
        }
        System.out.println("Now trying to update UI");
        UI ui = new UI();
        ui.entry(tokenscanner.pointset);
    }
}
