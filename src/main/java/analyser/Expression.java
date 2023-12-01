package main.java.analyser;

import main.java.nodes.*;

public class Expression {
    //后序遍历节点需要维护变量T处处相等
    public double Expression2Value(Node root, double T) {
        //常数节点
        if (root instanceof ConstNumNode) {
            return ((ConstNumNode) root).val;
        }
        //函数节点
        else if (root instanceof FunctionNode) {
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
        }
        //变量节点
        else if (root instanceof TNode) {
            return T;
        }
        //二元运算符节点
        else {
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
            }
        }
        throw new RuntimeException("表达式无法返回结果");
    }
}
