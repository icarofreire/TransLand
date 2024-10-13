package org.example.app;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Production {
    private final String leftSide;
    private final List<String> rightSide;

    public Production(String leftSide, List<String> rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    public String getLeftSide() {
        return leftSide;
    }

    public List<String> getRightSide() {
        return Collections.unmodifiableList(rightSide);
    }

    @Override
    public String toString() {
        return leftSide + " -> " + rightSide.stream().map(Object::toString).collect(Collectors.joining(""));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Production that = (Production) o;
        return Objects.equals(leftSide, that.leftSide) && Objects.equals(rightSide, that.rightSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftSide, rightSide);
    }
}

