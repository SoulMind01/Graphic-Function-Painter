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
            throw new RuntimeException("Token not found");
        }
        // get the first token
        Scan();
        Analyse();
    }

    void Scan() throws IOException {
        tmpToken = s.GetToken();
        if (tmpToken.type == TokenTypes.ERROR_TOKEN) {
            System.out.println("name:" + tmpToken.name + " val:" + tmpToken.val);
            throw new RuntimeException("Unexpected token");
        }
    }

    // call the parser
    void Analyse() throws IOException {
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
                throw new RuntimeException("Syntax error");
        }
    }

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
        if (step == 0) {
            throw new RuntimeException("Step size cannot be zero");
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
            pointtransformer.InsertPoint(pointset, expression.Expression2Value(x, i),
                    expression.Expression2Value(y, i));
        }
    }

    void MatchToken(TokenTypes Type) {
        if (tmpToken.type != Type) {
            throw new RuntimeException("Syntax error");
        }
    }

    void MatchToken(TokenTypes Type, int x) throws IOException {
        if (tmpToken.type != Type) {
            throw new RuntimeException("Syntax error");
        }
        Scan();
    }

    Node Token2Expression() throws IOException {
        Node lChild, rChild;
        TokenTypes NowType;
        lChild = Token2Term();
        while (tmpToken.type == TokenTypes.PLUS || tmpToken.type == TokenTypes.MINUS) {
            NowType = tmpToken.type;
            Scan();
            rChild = Token2Term();
            lChild = new TwoOperatorNode(NowType, lChild, rChild);
        }
        return lChild;
    }

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

    Node Token2Factor() throws IOException {
        Node Child;
        int Negative = 1;
        if (tmpToken.type == TokenTypes.PLUS) {
            Scan();
        } else if (tmpToken.type == TokenTypes.MINUS) {
            Negative *= -1;
            Scan();
        }

        Child = Token2Component();
        return Negative == 1 ? Child
                : new TwoOperatorNode(TokenTypes.MINUS, new ConstNumNode(TokenTypes.CONST_NUM, 0), Child);
    }

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
                throw new RuntimeException("Syntax error at Token2Atom");
        }
    }

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
