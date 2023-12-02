package main.java.analyser;

import main.java.nodes.*;

public class Expression {
    // Post-order traversal of nodes requires maintaining the variable T equal
    // everywhere
    public double Expression2Value(Node root, double T) {
        if (root instanceof ConstNumNode) {
            return ((ConstNumNode) root).val;
        } else if (root instanceof FunctionNode) {
            double ans = Expression2Value(((FunctionNode) root).child, T);
            switch (((FunctionNode) root).FunctionType) {
                case "SIN":
                    return Math.sin(ans);
                case "COS":
                    return Math.cos(ans);
                case "TAN":
                    return Math.tan(ans);
                case "SQRT":
                    return Math.sqrt(ans);
                case "EXP":
                    return Math.exp(ans);
                case "LN":
                    return Math.log(ans);
            }
        } else if (root instanceof TNode) {
            return T;
        } else {
            double lNum = Expression2Value(((TwoOperatorNode) root).lChild, T);
            double rNum = Expression2Value(((TwoOperatorNode) root).rChild, T);
            switch (root.Type) {
                case PLUS:
                    return lNum + rNum;
                case MINUS:
                    return lNum - rNum;
                case MUL:
                    return lNum * rNum;
                case DIV:
                    return lNum / rNum;
                case POWER:
                    return Math.pow(lNum, rNum);
                default:
                    throw new RuntimeException("Expression has a exception type");
            }
        }
        throw new RuntimeException("Expression has a exception type");
    }
}
