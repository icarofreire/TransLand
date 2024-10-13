package org.example.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DottedProduction {
    // private static class DotSymbol implements Symbol {
    //     @Override
    //     public String toString() {
    //         return "·";
    //     }
    // }

    private final int dotIndex;
    private final Production production;

    public DottedProduction(final Production production, final int dotIndex) {
        if (dotIndex < 0 || dotIndex > production.getRightSide().size())
            throw new IllegalArgumentException("Invalid dot index.");

        this.production = production;
        this.dotIndex = dotIndex;
    }

    public DottedProduction advanceDot() {
        return new DottedProduction(this.production, this.dotIndex + 1);
    }

    public String getLeftSide() {
        return this.production.getLeftSide();
    }

    public String getExpectedSymbol() {
        if (hasEndingDot())
            throw new UnsupportedOperationException("Dot is at the end.");

        return production.getRightSide().get(dotIndex);
    }

    public boolean hasEndingDot() {
        return this.dotIndex == production.getRightSide().size();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DottedProduction that = (DottedProduction) o;
        return dotIndex == that.dotIndex &&
                Objects.equals(production, that.production);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dotIndex, production);
    }

    @Override
    public String toString() {
        final List<String> rightSide = new ArrayList<>(production.getRightSide());
        // rightSide.add(dotIndex, new DotSymbol());
        final Production production = new Production(this.production.getLeftSide(), rightSide);

        return production.toString();
    }
}

