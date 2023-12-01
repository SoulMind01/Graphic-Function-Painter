package main.java.nodes;

import main.java.str.TokenTypes;

public class ConstNumNode extends Node {
    public double val;

    //构造常量节点需要储存数值
    public ConstNumNode(TokenTypes Type, double val) {
        super(Type);
        this.val = val;
    }
}
