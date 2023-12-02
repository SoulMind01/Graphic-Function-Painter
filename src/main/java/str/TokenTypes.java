package main.java.str;

import java.util.HashMap;
import java.util.Map;

//分析器中的记号类型
public enum TokenTypes {
    // const number
    CONST_NUM,
    // variable
    T,
    // function
    FUNC,
    // keyword
    ORIGIN, SCALE, ROT, IS, FOR, FROM, TO, STEP, DRAW,
    // operator
    PLUS, MINUS, MUL, DIV, POWER,
    // separator
    SEMICOLON, L_BRACKET, R_BRACKET, COMMA,
    // unexpected token
    ERROR_TOKEN,
    // end of file
    END_TOKEN,
    // comment
    COMMENT,
    // word
    ID;

    public static Map<String, Token> String2TokenTable = new HashMap<>();

    static {
        // const number
        String2TokenTable.put("PI", new Token(CONST_NUM, "PI", 3.1415926535));
        String2TokenTable.put("E", new Token(CONST_NUM, "E", 2.71828));
        // variable
        String2TokenTable.put("T", new Token(T, "T", 0));
        // function
        String2TokenTable.put("SIN", new Token(FUNC, "SIN", 0));
        String2TokenTable.put("COS", new Token(FUNC, "COS", 0));
        String2TokenTable.put("TAN", new Token(FUNC, "TAN", 0));
        String2TokenTable.put("SQRT", new Token(FUNC, "SQRT", 0));
        String2TokenTable.put("EXP", new Token(FUNC, "EXP", 0));
        String2TokenTable.put("LN", new Token(FUNC, "LN", 0));
        // keyword
        String2TokenTable.put("ORIGIN", new Token(ORIGIN, "ORIGIN", 0));
        String2TokenTable.put("SCALE", new Token(SCALE, "SCALE", 0));
        String2TokenTable.put("ROT", new Token(ROT, "ROT", 0));
        String2TokenTable.put("IS", new Token(IS, "IS", 0));
        String2TokenTable.put("FOR", new Token(FOR, "FOR", 0));
        String2TokenTable.put("FROM", new Token(FROM, "FROM", 0));
        String2TokenTable.put("TO", new Token(TO, "TO", 0));
        String2TokenTable.put("STEP", new Token(STEP, "STEP", 0));
        String2TokenTable.put("DRAW", new Token(DRAW, "DRAW", 0));
        // operator
        String2TokenTable.put("+", new Token(PLUS, "+", 0));
        String2TokenTable.put("-", new Token(MINUS, "-", 0));
        String2TokenTable.put("*", new Token(MUL, "*", 0));
        String2TokenTable.put("/", new Token(DIV, "/", 0));
        String2TokenTable.put("**", new Token(POWER, "**", 0));
        // separator
        String2TokenTable.put(";", new Token(SEMICOLON, ";", 0));
        String2TokenTable.put("(", new Token(L_BRACKET, "(", 0));
        String2TokenTable.put(")", new Token(R_BRACKET, ")", 0));
        String2TokenTable.put(",", new Token(COMMA, ",", 0));
        // unexpected token
        String2TokenTable.put("" + TextScanner.EOF, new Token(END_TOKEN, "EOF", 0));
        // comment
        String2TokenTable.put("//", new Token(COMMENT, "//", 0));
        String2TokenTable.put("--", new Token(COMMENT, "--", 0));
    }

    public static Token String2Token(String s) {
        Token t = String2TokenTable.get(s);
        return t == null ? new Token(ERROR_TOKEN, s, 0) : t;
    }
}
