package org.example.app;

import java.util.Objects;

public class State {
    private final DottedProduction production;
    private final int startIndex;
    private final ASTNode parseTree;

    public State(final DottedProduction production, final int startIndex, final ASTNode parseTree) {
        this.production = production;
        this.startIndex = startIndex;
        this.parseTree = parseTree;
    }

    public DottedProduction getProduction() {
        return production;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public ASTNode getParseTree() {
        return parseTree;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final State state = (State) o;
        return startIndex == state.startIndex && Objects.equals(production, state.production);
    }

    @Override
    public int hashCode() {
        return Objects.hash(production, startIndex);
    }

    @Override
    public String toString() {
        return "(" + production + ", " + startIndex + ")";
    }
}

