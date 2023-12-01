package main.java.str;

import java.io.IOException;
import java.io.InputStreamReader;

public class TextScanner {
    //当前字符
    public char tmpChar;
    //代表输入流的结束
    public static char EOF = (char) -1;
    //输入流
    InputStreamReader input;

    //当前处理的字符
    public TextScanner(InputStreamReader input)throws IOException {
        this.input = input;
        //构造该对象时必须读取一个字符，否则程序无法初始化
        Scan();
    }

    public void Scan() throws IOException {
        //读取一个字符，字母全部转换为大写
        int NowNum=input.read();
        tmpChar = Character.toUpperCase((char)NowNum);
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
