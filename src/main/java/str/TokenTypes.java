package main.java.str;

import java.util.HashMap;
import java.util.Map;

//分析器中的记号类型
public enum TokenTypes {
    //常数
    CONST_NUM,
    //参数
    T,
    //函数，之后再细分
    FUNC,
    //保留字
    ORIGIN, SCALE, ROT, IS, FOR, FROM, TO, STEP, DRAW,
    //运算符
    PLUS, MINUS, MUL, DIV, POWER,
    //分隔符
    SEMICOLON, L_BRACKET, R_BRACKET, COMMA,
    //非法输入
    ERROR_TOKEN,
    //终止
    END_TOKEN,
    //注释
    COMMENT,
    //单词
    ID;
    //建立从字符串到Token的映射
    public static Map<String, Token> String2TokenTable = new HashMap<>();

    //整个程序中只储存一次映射表
    static {
        //常数
        String2TokenTable.put("PI", new Token(CONST_NUM, "PI", 3.1415926535));
        String2TokenTable.put("E", new Token(CONST_NUM, "E", 2.71828));
        //参数
        String2TokenTable.put("T", new Token(T, "T", 0));
        //函数
        String2TokenTable.put("SIN", new Token(FUNC, "SIN", 0));
        String2TokenTable.put("COS", new Token(FUNC, "COS", 0));
        String2TokenTable.put("TAN", new Token(FUNC, "TAN", 0));
        String2TokenTable.put("SQRT", new Token(FUNC, "SQRT", 0));
        String2TokenTable.put("EXP", new Token(FUNC, "EXP", 0));
        String2TokenTable.put("LN", new Token(FUNC, "LN", 0));
        //保留字
        String2TokenTable.put("ORIGIN", new Token(ORIGIN, "ORIGIN", 0));
        String2TokenTable.put("SCALE", new Token(SCALE, "SCALE", 0));
        String2TokenTable.put("ROT", new Token(ROT, "ROT", 0));
        String2TokenTable.put("IS", new Token(IS, "IS", 0));
        String2TokenTable.put("FOR", new Token(FOR, "FOR", 0));
        String2TokenTable.put("FROM", new Token(FROM, "FROM", 0));
        String2TokenTable.put("TO", new Token(TO, "TO", 0));
        String2TokenTable.put("STEP", new Token(STEP, "STEP", 0));
        String2TokenTable.put("DRAW", new Token(DRAW, "DRAW", 0));
        //运算符
        String2TokenTable.put("+", new Token(PLUS, "+", 0));
        String2TokenTable.put("-", new Token(MINUS, "-", 0));
        String2TokenTable.put("*", new Token(MUL, "*", 0));
        String2TokenTable.put("/", new Token(DIV, "/", 0));
        String2TokenTable.put("**", new Token(POWER, "**", 0));
        //分隔符
        String2TokenTable.put(";", new Token(SEMICOLON, ";", 0));
        String2TokenTable.put("(", new Token(L_BRACKET, "(", 0));
        String2TokenTable.put(")", new Token(R_BRACKET, ")", 0));
        String2TokenTable.put(",", new Token(COMMA, ",", 0));
        //终止
        String2TokenTable.put("" + TextScanner.EOF, new Token(END_TOKEN, "EOF", 0));
        //注释
        String2TokenTable.put("//", new Token(COMMENT, "//", 0));
        String2TokenTable.put("--", new Token(COMMENT, "--", 0));
    }

    public static Token String2Token(String s) {
        Token t = String2TokenTable.get(s);
        //不属于映射表的为非法输入
        return t == null ? new Token(ERROR_TOKEN, s, 0) : t;
    }
}
