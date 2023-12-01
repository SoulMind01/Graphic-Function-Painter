package main.java.nodes;

import main.java.str.TokenTypes;

public class FunctionNode extends Node {
    public Node child;
    public String FunctionType;

    //构造函数节点需要储存子节点和函数名
    public FunctionNode(TokenTypes Type, Node child, String FunctionType) {
        super(Type);
        this.child = child;
        this.FunctionType = FunctionType;
    }
}
