package main.java.nodes;

import main.java.str.TokenTypes;

public class TwoOperatorNode extends Node {
    public Node lChild, rChild;

    //构造二元运算符节点需要储存左右子节点
    public TwoOperatorNode(TokenTypes Type, Node lChild, Node rChild) {
        super(Type);
        this.lChild = lChild;
        this.rChild = rChild;
    }
}
