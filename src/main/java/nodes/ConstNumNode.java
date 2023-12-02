package main.java.nodes;

import main.java.str.TokenTypes;

public class ConstNumNode extends Node {
    public double val;

    public ConstNumNode(TokenTypes Type, double val) {
        super(Type);
        this.val = val;
    }
}
