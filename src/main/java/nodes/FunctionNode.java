package main.java.nodes;

import main.java.str.TokenTypes;

public class FunctionNode extends Node {
    public Node child;
    public String FunctionType;

    public FunctionNode(TokenTypes Type, Node child, String FunctionType) {
        super(Type);
        this.child = child;
        this.FunctionType = FunctionType;
    }
}
