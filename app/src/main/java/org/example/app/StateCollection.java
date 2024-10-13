package org.example.app;

import java.util.*;
import java.util.stream.Collectors;

public class StateCollection {
    private final Set<State> states;
    private final Queue<State> unvisited;

    public StateCollection() {
        this.states = new HashSet<>();
        this.unvisited = new ArrayDeque<>();
    }

    public void addState(final State state) {
        if (!states.contains(state)) {
            states.add(state);
            unvisited.add(state);
        }
    }

    public boolean hasUnvisitedStates() {
        return unvisited.size() > 0;
    }

    public State getUnvisitedState() {
        return unvisited.remove();
    }

    public Set<State> getStatesExpecting(final String nonTerminal) {
        return states.stream()
                .filter((state) -> !state.getProduction().hasEndingDot() &&
                        state.getProduction().getExpectedSymbol().equals(nonTerminal))
                .collect(Collectors.toSet());
    }

    public boolean contains(final State state) {
        return states.contains(state);
    }

    public Optional<State> getStateMatching(final DottedProduction production, final int startIndex) {
        return this.states.stream().findFirst();
    }

    public Optional<State> getFirstState() {
        return this.states.stream().findFirst();
    }

    @Override
    public String toString() {
        return "StateCollection{ " +
                states.stream().map(Object::toString).collect(Collectors.joining(", ")) +
                " }";
    }
}

