package main.java.analyser;

import main.java.nodes.*;
import main.java.point.PointSet;
import main.java.point.PointTransformer;
import main.java.str.Token;
import main.java.str.TokenTypes;
import main.java.str.str;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TokenScanner {
    Token tmpToken;
    str s;
    Expression expression = new Expression();
    PointTransformer pointtransformer = new PointTransformer();
    public PointSet pointset = new PointSet();
    double ans;

    public void init(String File) throws FileNotFoundException, IOException {
        s = str.strSource(File);
        if (s == null) {
            throw new RuntimeException("没有记号流");
        }
        // 先取一个记号再开始分析
        Scan();
        Analyse();
    }

    void Scan() throws IOException {
        tmpToken = s.GetToken();
        if (tmpToken.type == TokenTypes.ERROR_TOKEN) {
            System.out.println("name:" + tmpToken.name + " val:" + tmpToken.val);
            throw new RuntimeException("词法错误");
        }
    }

    // 语法分析
    void Analyse() throws IOException {
        // 一直匹配语句到末尾
        while (tmpToken.type != TokenTypes.END_TOKEN) {
            MatchStatement();
            MatchToken(TokenTypes.SEMICOLON, 1);
        }
    }

    void MatchStatement() throws IOException {
        switch (tmpToken.type) {
            case ORIGIN:
                MatchORIGINStatement();
                break;
            case SCALE:
                MatchSCALEStatement();
                break;
            case ROT:
                MatchROTStatement();
                break;
            case FOR:
                MatchDRAWStatement();
                break;
            default:
                throw new RuntimeException("语法错误");
        }
    }

    // 识别ORIGIN语句
    void MatchORIGINStatement() throws IOException {
        MatchToken(TokenTypes.ORIGIN, 1);
        MatchToken(TokenTypes.IS, 1);
        MatchToken(TokenTypes.L_BRACKET, 1);
        double x = expression.Expression2Value(Token2Expression(), 0);
        MatchToken(TokenTypes.COMMA, 1);
        double y = expression.Expression2Value(Token2Expression(), 0);
        MatchToken(TokenTypes.R_BRACKET, 1);
        pointtransformer.SetOrigin(x, y);
    }

    void MatchSCALEStatement() throws IOException {
        MatchToken(TokenTypes.SCALE, 1);
        MatchToken(TokenTypes.IS, 1);
        MatchToken(TokenTypes.L_BRACKET, 1);
        double x = expression.Expression2Value(Token2Expression(), 0);
        MatchToken(TokenTypes.COMMA, 1);
        double y = expression.Expression2Value(Token2Expression(), 0);
        MatchToken(TokenTypes.R_BRACKET, 1);
        pointtransformer.SetScale(x, y);
    }

    void MatchROTStatement() throws IOException {
        MatchToken(TokenTypes.ROT, 1);
        MatchToken(TokenTypes.IS, 1);
        double Radium = expression.Expression2Value(Token2Expression(), 0);
        pointtransformer.SetRadium(Radium);
    }

    void MatchDRAWStatement() throws IOException, RuntimeException {
        MatchToken(TokenTypes.FOR, 1);
        MatchToken(TokenTypes.T, 1);
        MatchToken(TokenTypes.FROM, 1);
        double from = expression.Expression2Value(Token2Expression(), 0);
        MatchToken(TokenTypes.TO, 1);
        double to = expression.Expression2Value(Token2Expression(), 0);
        MatchToken(TokenTypes.STEP, 1);
        double step = expression.Expression2Value(Token2Expression(), 0);
        MatchToken(TokenTypes.DRAW, 1);
        MatchToken(TokenTypes.L_BRACKET, 1);
        // x，y是表达式，不能在这里求值，先建树
        Node x = Token2Expression();
        // debug
        System.out.println("Now printing x");
        visNodeTree(x, 1);
        MatchToken(TokenTypes.COMMA, 1);
        Node y = Token2Expression();
        // debug
        System.out.println("Now printing y");
        visNodeTree(y, 1);
        MatchToken(TokenTypes.R_BRACKET, 1);
        // 方便后续代码运行，统一往X轴正方向画
        if (step == 0) {
            throw new RuntimeException("步长必须为非0常数");
        }
        if (step < 0) {
            step *= -1;
        }
        if (from > to) {
            double tmp = from;
            from = to;
            to = tmp;
        }
        for (double i = from; i <= to; i += step) {
            // 把每个点带入表达式求值，坐标变换在InsertPoint方法中完成，因为此程序为读一句解释一句，所以这里可以认为已经设置好相关参数
            pointtransformer.InsertPoint(pointset, expression.Expression2Value(x, i),
                    expression.Expression2Value(y, i));
        }
    }

    /**
     * boolean MatchToken(TokenTypes Type) {
     * return tmpToken.type == Type;
     * }
     */
    void MatchToken(TokenTypes Type) {
        if (tmpToken.type != Type) {
            throw new RuntimeException("语法错误");
        }
    }

    void MatchToken(TokenTypes Type, int x) throws IOException {
        if (tmpToken.type != Type) {
            throw new RuntimeException("语法错误");
        }
        Scan();
    }

    // 处理加减
    Node Token2Expression() throws IOException {
        Node lChild, rChild;
        TokenTypes NowType;
        lChild = Token2Term();
        while (tmpToken.type == TokenTypes.PLUS || tmpToken.type == TokenTypes.MINUS) {
            NowType = tmpToken.type;
            Scan();
            rChild = Token2Term();
            // 左右节点结合成新树赋给左节点
            lChild = new TwoOperatorNode(NowType, lChild, rChild);
        }
        return lChild;
    }

    // 处理乘除
    Node Token2Term() throws IOException {
        Node lChild, rChild;
        TokenTypes NowType;
        lChild = Token2Factor();
        while (tmpToken.type == TokenTypes.MUL || tmpToken.type == TokenTypes.DIV) {
            NowType = tmpToken.type;
            Scan();
            rChild = Token2Factor();
            lChild = new TwoOperatorNode(NowType, lChild, rChild);
        }
        return lChild;
    }

    // 处理正负号
    Node Token2Factor() throws IOException {
        Node Child;
        int Negative = 1;
        // 正号直接跳过
        if (tmpToken.type == TokenTypes.PLUS) {
            Scan();
        }
        // 翻转符号位
        else if (tmpToken.type == TokenTypes.MINUS) {
            Negative *= -1;
            Scan();
        }

        Child = Token2Component();
        return Negative == 1 ? Child
                : new TwoOperatorNode(TokenTypes.MINUS, new ConstNumNode(TokenTypes.CONST_NUM, 0), Child);
    }

    // 处理幂
    Node Token2Component() throws IOException {
        Node lChild, rChild;
        lChild = Token2Atom();
        if (tmpToken.type == TokenTypes.POWER) {
            Scan();
            rChild = Token2Component();
            return new TwoOperatorNode(TokenTypes.POWER, lChild, rChild);
        }
        return lChild;
    }

    // 处理剩下的记号
    Node Token2Atom() throws IOException {
        Node Child;
        switch (tmpToken.type) {
            case CONST_NUM:
                Child = new ConstNumNode(TokenTypes.CONST_NUM, tmpToken.val);
                Scan();
                return Child;
            case T:
                Scan();
                return new TNode(TokenTypes.T);
            case L_BRACKET:
                Scan();
                Child = Token2Expression();
                MatchToken(TokenTypes.R_BRACKET, 1);
                return Child;
            case FUNC:
                String FunctionType = tmpToken.name;
                Scan();
                MatchToken(TokenTypes.L_BRACKET, 1);
                Node tmpChild = Token2Expression();
                MatchToken(TokenTypes.R_BRACKET, 1);
                return new FunctionNode(TokenTypes.FUNC, tmpChild, FunctionType);
            default:
                throw new RuntimeException("Atom语法错误");
        }
    }

    // 辅助程序，用于打印表达式节点树
    void visNodeTree(Node Root, int Depth) {
        for (int i = 0; i < Depth; i++) {
            System.out.print("\t|");
        }
        if (Root instanceof ConstNumNode) {
            System.out.println(((ConstNumNode) Root).val);
        } else if (Root instanceof FunctionNode) {
            System.out.println(((FunctionNode) Root).FunctionType);
            visNodeTree(((FunctionNode) Root).child, Depth + 1);
        } else if (Root instanceof TNode) {
            System.out.println(((TNode) Root).Type);
        } else {
            System.out.println(((TwoOperatorNode) Root).Type);
            visNodeTree(((TwoOperatorNode) Root).lChild, Depth + 1);
            visNodeTree(((TwoOperatorNode) Root).rChild, Depth + 1);
        }
    }
}
