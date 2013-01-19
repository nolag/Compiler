//TODO this should be auto gen from rules somehow, or made as rules?
package cs444.parser.rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cs444.lexer.Token;
import cs444.parser.IParserRule;
import cs444.parser.factory.ISymbolFactory;
import cs444.parser.factory.NonTerminalFactory;
import cs444.parser.factory.TerminalFactory;
import cs444.parser.symbols.StateTerminal;

public class TestRule implements IParserRule{
    private final NonTerminalFactory startFact;

    public TestRule() {
        TerminalFactory number = new TerminalFactory(Token.Type.DECIMAL_INTEGER_LITERAL);
        TerminalFactory declaration = new TerminalFactory(Token.Type.INT);
        TerminalFactory identifier = new TerminalFactory(Token.Type.ID);
        TerminalFactory equals = new TerminalFactory(Token.Type.EQ);
        TerminalFactory plus = new TerminalFactory(Token.Type.PLUS);
        TerminalFactory minus = new TerminalFactory(Token.Type.MINUS);

        Map<Integer, Map<Token.Type, StateTerminal>> declsRules = new HashMap<Integer, Map<Token.Type, StateTerminal>>();
        Set<Integer> declAccepting = new HashSet<Integer>();

        NonTerminalFactory decl = new NonTerminalFactory(declsRules, declAccepting, "decl");

        Map<Token.Type, StateTerminal> declRules0 = new HashMap<Token.Type, StateTerminal>();
        declRules0.put(Token.Type.INT, new StateTerminal(declaration, 1));

        Map<Token.Type, StateTerminal> declRules1 = new HashMap<Token.Type, StateTerminal>();
        declRules1.put(Token.Type.ID, new StateTerminal(identifier, 2));

        Map<Token.Type, StateTerminal> declRules2 = new HashMap<Token.Type, StateTerminal>();
        declRules2.put(Token.Type.EQ, new StateTerminal(equals, 3));

        Map<Token.Type, StateTerminal> declRules3 = new HashMap<Token.Type, StateTerminal>();
        declRules3.put(Token.Type.ID, new StateTerminal(identifier, 4));
        declRules3.put(Token.Type.DECIMAL_INTEGER_LITERAL, new StateTerminal(number, 4));

        Map<Token.Type, StateTerminal> declRules4 = new HashMap<Token.Type, StateTerminal>();
        declRules4.put(Token.Type.INT, new StateTerminal(decl, 5));
        declRules4.put(Token.Type.EMPTY, new StateTerminal(decl, 5));

        Map<Token.Type, StateTerminal> declRules5 = new HashMap<Token.Type, StateTerminal>();

        declsRules.put(0, declRules0);
        declsRules.put(1, declRules1);
        declsRules.put(2, declRules2);
        declsRules.put(3, declRules3);
        declsRules.put(4, declRules4);
        declsRules.put(5, declRules5);

        declAccepting.add(0);
        declAccepting.add(5);

        Map<Integer, Map<Token.Type, StateTerminal>> becomesRules = new HashMap<Integer, Map<Token.Type, StateTerminal>>();

        Set<Integer> becomesAccepting = new HashSet<Integer>();
        NonTerminalFactory becomes = new NonTerminalFactory(becomesRules, becomesAccepting, "becomes");

        Map<Token.Type, StateTerminal> becomesRules0 = new HashMap<Token.Type, StateTerminal>();
        becomesRules0.put(Token.Type.ID, new StateTerminal(identifier, 1));

        Map<Token.Type, StateTerminal> becomesRules1 = new HashMap<Token.Type, StateTerminal>();
        becomesRules1.put(Token.Type.EQ, new StateTerminal(equals, 2));
        becomesRules1.put(Token.Type.PLUS, new StateTerminal(plus, 3));
        becomesRules1.put(Token.Type.MINUS, new StateTerminal(minus, 3));

        Map<Token.Type, StateTerminal> becomesRules2 = new HashMap<Token.Type, StateTerminal>();
        becomesRules2.put(Token.Type.ID, new StateTerminal(identifier, 5));
        becomesRules2.put(Token.Type.DECIMAL_INTEGER_LITERAL, new StateTerminal(number, 5));

        Map<Token.Type, StateTerminal> becomesRules3 = new HashMap<Token.Type, StateTerminal>();
        becomesRules3.put(Token.Type.EQ, new StateTerminal(equals, 4));

        Map<Token.Type, StateTerminal> becomesRules4 = new HashMap<Token.Type, StateTerminal>();
        becomesRules4.put(Token.Type.ID, new StateTerminal(identifier, 5));
        becomesRules4.put(Token.Type.DECIMAL_INTEGER_LITERAL, new StateTerminal(number, 5));


        Map<Token.Type, StateTerminal> becomesRules5 = new HashMap<Token.Type, StateTerminal>();
        becomesRules5.put(Token.Type.ID, new StateTerminal(becomes, 6));
        becomesRules5.put(Token.Type.DECIMAL_INTEGER_LITERAL, new StateTerminal(becomes, 6));
        becomesRules5.put(Token.Type.EMPTY, new StateTerminal(becomes, 6));

        Map<Token.Type, StateTerminal> becomesRules6 = new HashMap<Token.Type, StateTerminal>();

        becomesAccepting.add(0);
        becomesAccepting.add(6);

        becomesRules.put(0, becomesRules0);
        becomesRules.put(1, becomesRules1);
        becomesRules.put(2, becomesRules2);
        becomesRules.put(3, becomesRules3);
        becomesRules.put(4, becomesRules4);
        becomesRules.put(5, becomesRules5);
        becomesRules.put(6, becomesRules6);

        Map<Integer, Map<Token.Type, StateTerminal>> becomesOrDclRules = new HashMap<Integer, Map<Token.Type, StateTerminal>>();
        Set<Integer> becomesOrDclAccepting = new HashSet<Integer>();
        NonTerminalFactory becomesOrDcl = new NonTerminalFactory(becomesOrDclRules, becomesOrDclAccepting, "declBecomes");

        Map<Token.Type, StateTerminal> becomesOrDclRules0 = new HashMap<Token.Type, StateTerminal>();
        becomesOrDclRules0.put(Token.Type.INT, new StateTerminal(decl, 1));
        becomesOrDclRules0.put(Token.Type.ID, new StateTerminal(becomes, 3));

        Map<Token.Type, StateTerminal> becomesOrDclRules1 = new HashMap<Token.Type, StateTerminal>();
        becomesOrDclRules1.put(Token.Type.ID, new StateTerminal(becomes, 2));

        Map<Token.Type, StateTerminal> becomesOrDclRules2 = new HashMap<Token.Type, StateTerminal>();
        Map<Token.Type, StateTerminal> becomesOrDclRules3 = new HashMap<Token.Type, StateTerminal>();

        becomesOrDclRules.put(0, becomesOrDclRules0);
        becomesOrDclRules.put(1, becomesOrDclRules1);
        becomesOrDclRules.put(2, becomesOrDclRules2);
        becomesOrDclRules.put(3, becomesOrDclRules3);

        becomesOrDclAccepting.add(2);
        becomesOrDclAccepting.add(3);

        startFact = becomesOrDcl;
    }

    public ISymbolFactory getStartSymbol() {
        return startFact;
    }
}