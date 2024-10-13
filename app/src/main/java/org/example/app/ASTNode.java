package org.example.app;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ASTNode {
    private final String symbol;
    private final List<ASTNode> children;

    public ASTNode(final String symbol, final List<ASTNode> children) {
        this.symbol = symbol;
        this.children = children;
    }

    public ASTNode addChild(final ASTNode child) {
        final ArrayList<ASTNode> newChildren = new ArrayList<>(children);
        newChildren.add(child);
        return new ASTNode(symbol, new ArrayList<ASTNode>(newChildren));
    }

    public String getSymbol() {
        return symbol;
    }

    public List<ASTNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ASTNode astNode = (ASTNode) o;
        return Objects.equals(symbol, astNode.symbol) &&
                Objects.equals(children, astNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, children);
    }
}


