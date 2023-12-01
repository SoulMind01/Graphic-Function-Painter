package main.java;

import main.java.analyser.TokenScanner;

import java.io.FileNotFoundException;
import java.io.IOException;

//程序驱动类
public class Driver {
    public void StartDriving(String File) {
        System.out.println("The program is running\n");
        TokenScanner tokenscanner = new TokenScanner();
        try {
            tokenscanner.init(File);
            System.out.println("Now scanning file " + File);
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
        } catch (IOException e) {
            System.out.println("IO异常");
        }
        System.out.println("Now trying to update UI");
        UI ui = new UI();
        ui.entry(tokenscanner.pointset);
    }
}
