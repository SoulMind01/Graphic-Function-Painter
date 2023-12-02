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

    // 通过文件名构造输入字符对象
    public static str strSource(String file) throws FileNotFoundException, IOException {
        FileInputStream input = new FileInputStream(file);
        return strSource(input);
    }

    // 通过文件输入流构造输入字符对象
    public static str strSource(FileInputStream input) throws IOException {
        InputStreamReader inputstreamreader = new InputStreamReader(input);
        TextScanner textScanner1 = new TextScanner(inputstreamreader);
        return new str(textScanner1);
    }

    // 尝试读取一行的内容
    public Token GetToken() throws IOException {
        // EOF特判
        if (textscanner.tmpChar == TextScanner.EOF) {
            textscanner.Close();
            return TokenTypes.String2Token("" + TextScanner.EOF);
        }
        // 如果是空格换行，回车，tab则跳过
        while (textscanner.tmpChar == ' ' || textscanner.tmpChar == '\t' || textscanner.tmpChar == '\n'
                || textscanner.tmpChar == '\r') {
            textscanner.Scan();
        }

        // 如果第一个字符是数字，则尝试识别常数，读取相连的常数所有包含的字符
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
        // 如果第一个字符是字母，则尝试识别保留字，参数，常数，函数，读取所有相连的字母和数字
        else if (IsAlpha(textscanner.tmpChar)) {
            StringBuffer s = new StringBuffer();
            while (IsAlpha(textscanner.tmpChar)) {
                s.append(textscanner.tmpChar);
                textscanner.Scan();
            }
            return TokenTypes.String2Token(String.valueOf(s));
        }
        // 其他情况则尝试识别运算符，分隔符
        else {
            // 尝试识别单字符运算符
            Token t = TokenTypes.String2Token("" + textscanner.tmpChar);
            textscanner.Scan();
            // 尝试识别幂
            if (t.type == TokenTypes.MUL && textscanner.tmpChar == '*') {
                t = TokenTypes.String2Token("**");
                textscanner.Scan();
            }
            // 尝试识别注释
            else if ((t.type == TokenTypes.DIV && textscanner.tmpChar == '/')
                    || (t.type == TokenTypes.MINUS && textscanner.tmpChar == '-')) {
                // 跳过注释的内容，EOF特判
                while (textscanner.tmpChar != '\n' && textscanner.tmpChar != '\r'
                        && textscanner.tmpChar != TextScanner.EOF) {
                    textscanner.Scan();
                }
                // 找到注释要寻找下一个标记返回
                return GetToken();
            }
            // 返回原本记号
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
