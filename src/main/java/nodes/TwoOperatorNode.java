package main.java.nodes;

import main.java.str.TokenTypes;

public class TwoOperatorNode extends Node {
    public Node lChild, rChild;

    public TwoOperatorNode(TokenTypes Type, Node lChild, Node rChild) {
        super(Type);
        this.lChild = lChild;
        this.rChild = rChild;
    }
}
