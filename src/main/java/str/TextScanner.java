package main.java.str;

import java.io.IOException;
import java.io.InputStreamReader;

public class TextScanner {
    // the current character
    public char tmpChar;

    public static char EOF = (char) -1;

    InputStreamReader input;

    public TextScanner(InputStreamReader input) throws IOException {
        this.input = input;
        // technically, the first character is not read yet
        Scan();
    }

    public void Scan() throws IOException {
        // transform the character to upper case
        int NowNum = input.read();
        tmpChar = Character.toUpperCase((char) NowNum);
    }

    public void Close() {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
