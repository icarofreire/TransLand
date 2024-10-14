package org.example.app;

import java.util.*;

public class EarleyParser {
    // private final class EarleyAxiom implements NonTerminal {
    //     @Override
    //     public String toString() {
    //         return "PHI";
    //     }
    // }
    private final String EARLEY_AXIOM = "PHI";

    private final Grammar grammar;
    private final Token token;
    private final List<StateCollection> stateCollections;

    public EarleyParser(final Grammar grammar, final Token token) {
        this.token = token;
        this.grammar = grammar;

        this.stateCollections = new ArrayList<>();
        this.stateCollections.add(createInitialState());
    }

    private StateCollection createInitialState() {
        final DottedProduction dottedProduction = getEarleyProduction(0);
        final StateCollection stateCollection = new StateCollection();
        stateCollection.addState(new State(dottedProduction, 0,
                new ASTNode(EARLEY_AXIOM, Collections.emptyList())));
        return stateCollection;
    }

    private DottedProduction getEarleyProduction(final int dotIndex) {
        final Production production = new Production(EARLEY_AXIOM,
                Arrays.asList(grammar.getStartSymbol()/*, token.getEOFTerminal()*/));
        return new DottedProduction(production, dotIndex);
    }

    public ASTNode parse(String input) /*throws ParsingErrorException*/ {
        Token token = null;
        int iteration = 0;

        Tokenizer tokenizer = new Tokenizer();
        List<String> listTokens = tokenizer.tokenizeInputForCode(input);

        // for(String strToken: listTokens){
            do {
                // try {
                    token = token.nextToken();
                // } catch (UnavailableTokenException e) {
                    // throw new ParsingErrorException();
                // }

                final StateCollection currentStates = stateCollections.get(iteration);

                while (currentStates.hasUnvisitedStates())
                    processState(currentStates.getUnvisitedState(), token, iteration);

                iteration++;
            } while (token != token.getEOFTerminal());
        // }

        return getResultingParseTree(iteration);
    }

    private ASTNode getResultingParseTree(int iteration) /*throws ParsingErrorException*/ {
        if (stateCollections.size() <= iteration){
            System.out.println("ParsingErrorException");
        }
            // throw new ParsingErrorException();

        final StateCollection finalStates = stateCollections.get(iteration);
        // final DottedProduction dottedProduction = getEarleyProduction(2);
        // final Optional<State> finalState = finalStates.getStateMatching(dottedProduction, 0);// << implementação antiga mal feita;
        final Optional<State> finalState = finalStates.getFirstState();
        if (finalState.isPresent())
            return removeEarleyProduction(finalState.get().getParseTree());

        // throw new ParsingErrorException();
        return null;
    }

    private ASTNode removeEarleyProduction(ASTNode tree) {
        return tree.getChildren().get(0);
    }

    public boolean isRecognized(String input) {
        // try {
        //     parse();
        //     return true;
        // } catch (ParsingErrorException e) {
        //     return false;
        // }
        return (parse(input) != null);
    }

    private void processState(final State state, final Token token, final int iteration) {
        if (state.getProduction().hasEndingDot())
            completer(state, iteration);
        else if (state.getProduction().getExpectedSymbol() instanceof String)
            scanner(state, token, iteration);
        else
            predictor(state, iteration);
    }

    private void completer(final State state, final int iteration) {
        final String producedSymbol = state.getProduction().getLeftSide();
        final StateCollection searchCollection = stateCollections.get(state.getStartIndex());

        for (final State s : searchCollection.getStatesExpecting(producedSymbol)) {
            final ASTNode parseTree = s.getParseTree().addChild(state.getParseTree());

            stateCollections.get(iteration).addState(
                    new State(s.getProduction().advanceDot(), s.getStartIndex(), parseTree));
        }
    }

    private void scanner(final State state, final Token currentToken, final int iteration) {
        final String expectedSymbol = (String) state.getProduction().getExpectedSymbol();
        if (expectedSymbol.equals(currentToken.asTerminal())) {// << ANALISAR ISSO AQUI;;;;;;

            if (stateCollections.size() == iteration + 1)
                stateCollections.add(new StateCollection());

            final ASTNode parseTree = state.getParseTree().addChild(new ASTNode(currentToken.getIdentifier(), Collections.emptyList()));

            stateCollections.get(iteration+1).addState(
                    new State(state.getProduction().advanceDot(), state.getStartIndex(), parseTree));
        }
    }

    private void predictor(final State state, final int iteration) {
        final String expectedSymbol = (String) state.getProduction().getExpectedSymbol();

        for (Production production : grammar.productionsFrom(expectedSymbol)) {
            final DottedProduction dottedProduction = new DottedProduction(production, 0);

            final ASTNode parseTree = new ASTNode(production.getLeftSide(), Collections.emptyList());

            stateCollections.get(iteration).addState(
                    new State(dottedProduction, iteration, parseTree));
        }
    }
}

