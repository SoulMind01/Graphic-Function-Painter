package main.java.nodes;

import main.java.str.TokenTypes;

public class Node {
    public TokenTypes Type;

    //构造节点类需要储存节点种类
    Node(TokenTypes Type) {
        this.Type = Type;
    }
}
