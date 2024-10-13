package org.example.app;


import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    // public static final Terminal EPSILON = new Terminal() {
    //     @Override
    //     public String toString() {
    //         return "ε";
    //     }
    // };

    private final Map<String, List<Production>> productions;
    private final String startSymbol;

    public Grammar(final String startSymbol, final Production... productions) {
        this.startSymbol = startSymbol;

        this.productions = new HashMap<>();
        for (Production production : productions) {
            final List<Production> prodList = this.productions.getOrDefault(production.getLeftSide(), new ArrayList<>());
            prodList.add(production);
            this.productions.putIfAbsent(production.getLeftSide(), prodList);
        }
    }

    public List<Production> productionsFrom(final String nonTerminal) {
        return Collections.unmodifiableList(this.productions.getOrDefault(nonTerminal, Collections.emptyList()));
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public List<Production> productions() {
        return productions.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}

