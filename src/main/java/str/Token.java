package main.java.str;

//需要用到的记号的属性可能是双精度浮点数或者字符串
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
