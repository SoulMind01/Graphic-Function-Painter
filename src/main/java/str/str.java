package main.java.str;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class str {
    TextScanner textscanner;

    public str(TextScanner textscanner) {
        this.textscanner = textscanner;
    }

    // initialize the input stream from a file
    public static str strSource(String file) throws FileNotFoundException, IOException {
        FileInputStream input = new FileInputStream(file);
        return strSource(input);
    }

    // initialize the input stream from a FileInputStream
    public static str strSource(FileInputStream input) throws IOException {
        InputStreamReader inputstreamreader = new InputStreamReader(input);
        TextScanner textScanner1 = new TextScanner(inputstreamreader);
        return new str(textScanner1);
    }

    // try to get the next token
    public Token GetToken() throws IOException {
        // EOF
        if (textscanner.tmpChar == TextScanner.EOF) {
            textscanner.Close();
            return TokenTypes.String2Token("" + TextScanner.EOF);
        }
        // ignore the space, tab, newline, carriage return
        while (textscanner.tmpChar == ' ' || textscanner.tmpChar == '\t' || textscanner.tmpChar == '\n'
                || textscanner.tmpChar == '\r') {
            textscanner.Scan();
        }

        // if the first character is a digit, try to recognize a number
        if (IsDigit(textscanner.tmpChar)) {
            StringBuffer s = new StringBuffer();
            while (textscanner.tmpChar == '.' || IsDigit(textscanner.tmpChar)) {
                s.append(textscanner.tmpChar);
                textscanner.Scan();
            }
            try {
                double d = Double.parseDouble(String.valueOf(s));
                return new Token(TokenTypes.CONST_NUM, String.valueOf(s), d);
            } catch (NumberFormatException e) {
                return new Token(TokenTypes.ERROR_TOKEN, String.valueOf(s), 0);
            }
        }
        // if the first character is a letter, try to recognize a keyword or a string
        else if (IsAlpha(textscanner.tmpChar)) {
            StringBuffer s = new StringBuffer();
            while (IsAlpha(textscanner.tmpChar)) {
                s.append(textscanner.tmpChar);
                textscanner.Scan();
            }
            return TokenTypes.String2Token(String.valueOf(s));
        }
        // if the first character is a symbol, try to recognize a symbol
        else {
            // unary operator
            Token t = TokenTypes.String2Token("" + textscanner.tmpChar);
            textscanner.Scan();
            // exponentiation operator
            if (t.type == TokenTypes.MUL && textscanner.tmpChar == '*') {
                t = TokenTypes.String2Token("**");
                textscanner.Scan();
            }
            // comment
            else if ((t.type == TokenTypes.DIV && textscanner.tmpChar == '/')
                    || (t.type == TokenTypes.MINUS && textscanner.tmpChar == '-')) {
                while (textscanner.tmpChar != '\n' && textscanner.tmpChar != '\r'
                        && textscanner.tmpChar != TextScanner.EOF) {
                    textscanner.Scan();
                }
                return GetToken();
            }
            // other symbols
            return t;
        }
    }

    boolean IsDigit(char c) {
        return c >= '0' && c <= '9';
    }

    boolean IsAlpha(char c) {
        return c >= 'A' && c <= 'Z';
    }
}
