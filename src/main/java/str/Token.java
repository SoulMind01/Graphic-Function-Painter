package main.java.str;

public class Token {
    public TokenTypes type;
    public String name;
    public double val;

    public Token(TokenTypes type, String name, double val) {
        this.type = type;
        this.name = name;
        this.val = val;
    }
}
