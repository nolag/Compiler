//TODO this should be auto gen from rules somehow, or made as rules?
package cs444.parser.rules;

import java.util.HashMap;
import java.util.Map;

import cs444.lexer.Token;
import cs444.parser.IParserRule;
import cs444.parser.symbols.StateTerminal;
import cs444.parser.symbols.factories.ISymbolFactory;
import cs444.parser.symbols.factories.NonTerminalFactory;
import cs444.parser.symbols.factories.TerminalFactory;

public class TestRule implements IParserRule{
    private final NonTerminalFactory startFact;

    public TestRule() {
        TerminalFactory number = new TerminalFactory(Token.Type.DECIMAL_INTEGER_LITERAL);
        TerminalFactory declaration = new TerminalFactory(Token.Type.INT);
        TerminalFactory identifier = new TerminalFactory(Token.Type.ID);
        TerminalFactory equals = new TerminalFactory(Token.Type.EQ);
        TerminalFactory plus = new TerminalFactory(Token.Type.PLUS);
        TerminalFactory minus = new TerminalFactory(Token.Type.MINUS);
        TerminalFactory semi = new TerminalFactory(Token.Type.SEMI);

        Map<Integer, Map<Token.Type, StateTerminal>> declsRules = new HashMap<Integer, Map<Token.Type, StateTerminal>>();
        Map<Integer, String> declAccepting = new HashMap<Integer, String>();

        NonTerminalFactory decl = new NonTerminalFactory(declsRules, declAccepting, "decl");

        Map<Token.Type, StateTerminal> declRules0 = new HashMap<Token.Type, StateTerminal>();
        declRules0.put(Token.Type.INT, new StateTerminal(declaration, 1));
        declRules0.put(Token.Type.ID, new StateTerminal(identifier, 1));

        Map<Token.Type, StateTerminal> declRules1 = new HashMap<Token.Type, StateTerminal>();
        declRules1.put(Token.Type.ID, new StateTerminal(identifier, 2));

        Map<Token.Type, StateTerminal> declRules2 = new HashMap<Token.Type, StateTerminal>();
        declRules2.put(Token.Type.EQ, new StateTerminal(equals, 3));

        Map<Token.Type, StateTerminal> declRules3 = new HashMap<Token.Type, StateTerminal>();
        declRules3.put(Token.Type.ID, new StateTerminal(identifier, 4));
        declRules3.put(Token.Type.DECIMAL_INTEGER_LITERAL, new StateTerminal(number, 4));

        Map<Token.Type, StateTerminal> declRules4 = new HashMap<Token.Type, StateTerminal>();
        declRules4.put(Token.Type.SEMI, new StateTerminal(semi, 5));

        Map<Token.Type, StateTerminal> declRules5 = new HashMap<Token.Type, StateTerminal>();
        declRules5.put(Token.Type.INT, new StateTerminal(decl, 6));

        Map<Token.Type, StateTerminal> declRules6 = new HashMap<Token.Type, StateTerminal>();

        declsRules.put(0, declRules0);
        declsRules.put(1, declRules1);
        declsRules.put(2, declRules2);
        declsRules.put(3, declRules3);
        declsRules.put(4, declRules4);
        declsRules.put(5, declRules5);
        declsRules.put(5, declRules6);

        declAccepting.put(0, "decl");
        declAccepting.put(5, "decl");
        declAccepting.put(6, "decl");

        Map<Integer, Map<Token.Type, StateTerminal>> becomesRules = new HashMap<Integer, Map<Token.Type, StateTerminal>>();

        Map<Integer, String> becomesAccepting = new HashMap<Integer, String>();
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

        Map<Token.Type, StateTerminal> becomesRules6 = new HashMap<Token.Type, StateTerminal>();

        becomesAccepting.put(0, "becomes");
        becomesAccepting.put(5, "becomes");
        becomesAccepting.put(6, "becomes");

        becomesRules.put(0, becomesRules0);
        becomesRules.put(1, becomesRules1);
        becomesRules.put(2, becomesRules2);
        becomesRules.put(3, becomesRules3);
        becomesRules.put(4, becomesRules4);
        becomesRules.put(5, becomesRules5);
        becomesRules.put(6, becomesRules6);

        Map<Integer, Map<Token.Type, StateTerminal>> _i_becomes_decl_Rules = new HashMap<Integer, Map<Token.Type, StateTerminal>>();

        Map<Integer, String> _i_becomes_decl_Accepting = new HashMap<Integer, String>();
        NonTerminalFactory _i_becomes_decl = new NonTerminalFactory(_i_becomes_decl_Rules, _i_becomes_decl_Accepting, "_i_becomes_or_decl");

        Map<Token.Type, StateTerminal> _i_becomes_decl0 = new HashMap<Token.Type, StateTerminal>();
        _i_becomes_decl0.put(Token.Type.ID, new StateTerminal(identifier, 1));
        _i_becomes_decl0.put(Token.Type.INT, new StateTerminal(declaration, 7));

        Map<Token.Type, StateTerminal> _i_becomes_decl1 = new HashMap<Token.Type, StateTerminal>();
        _i_becomes_decl1.put(Token.Type.EQ, new StateTerminal(equals, 2));
        _i_becomes_decl1.put(Token.Type.PLUS, new StateTerminal(plus, 3));
        _i_becomes_decl1.put(Token.Type.MINUS, new StateTerminal(minus, 3));
        _i_becomes_decl1.put(Token.Type.ID, new StateTerminal(equals, 8));

        Map<Token.Type, StateTerminal> _i_becomes_decl2 = new HashMap<Token.Type, StateTerminal>();
        _i_becomes_decl2.put(Token.Type.ID, new StateTerminal(identifier, 5));
        _i_becomes_decl2.put(Token.Type.DECIMAL_INTEGER_LITERAL, new StateTerminal(number, 5));

        Map<Token.Type, StateTerminal> _i_becomes_decl3 = new HashMap<Token.Type, StateTerminal>();
        _i_becomes_decl3.put(Token.Type.EQ, new StateTerminal(equals, 4));

        Map<Token.Type, StateTerminal> _i_becomes_decl4 = new HashMap<Token.Type, StateTerminal>();
        _i_becomes_decl4.put(Token.Type.ID, new StateTerminal(identifier, 5));
        _i_becomes_decl4.put(Token.Type.DECIMAL_INTEGER_LITERAL, new StateTerminal(number, 5));


        Map<Token.Type, StateTerminal> _i_becomes_decl5 = new HashMap<Token.Type, StateTerminal>();
        _i_becomes_decl5.put(Token.Type.ID, new StateTerminal(becomes, 6));
        _i_becomes_decl5.put(Token.Type.DECIMAL_INTEGER_LITERAL, new StateTerminal(becomes, 6));

        Map<Token.Type, StateTerminal> _i_becomes_decl6 = new HashMap<Token.Type, StateTerminal>();

        Map<Token.Type, StateTerminal> _i_becomes_decl7 = new HashMap<Token.Type, StateTerminal>();
        _i_becomes_decl7.put(Token.Type.ID, new StateTerminal(identifier, 8));

        Map<Token.Type, StateTerminal> _i_becomes_decl8 = new HashMap<Token.Type, StateTerminal>();
        _i_becomes_decl8.put(Token.Type.EQ, new StateTerminal(equals, 9));

        Map<Token.Type, StateTerminal> _i_becomes_decl9 = new HashMap<Token.Type, StateTerminal>();
        _i_becomes_decl9.put(Token.Type.ID, new StateTerminal(identifier, 10));
        _i_becomes_decl9.put(Token.Type.DECIMAL_INTEGER_LITERAL, new StateTerminal(number, 10));

        Map<Token.Type, StateTerminal> _i_becomes_decl10 = new HashMap<Token.Type, StateTerminal>();
        _i_becomes_decl10.put(Token.Type.SEMI, new StateTerminal(semi, 11));

        Map<Token.Type, StateTerminal> _i_becomes_decl11 = new HashMap<Token.Type, StateTerminal>();
        _i_becomes_decl11.put(Token.Type.INT, new StateTerminal(decl, 12));

        Map<Token.Type, StateTerminal> _i_becomes_decl12 = new HashMap<Token.Type, StateTerminal>();


        _i_becomes_decl_Accepting.put(0, "becomes");
        _i_becomes_decl_Accepting.put(5, "becomes");
        _i_becomes_decl_Accepting.put(6, "becomes");
        _i_becomes_decl_Accepting.put(11, "decl");
        _i_becomes_decl_Accepting.put(12, "decl");

        _i_becomes_decl_Rules.put(0, _i_becomes_decl0);
        _i_becomes_decl_Rules.put(1, _i_becomes_decl1);
        _i_becomes_decl_Rules.put(2, _i_becomes_decl2);
        _i_becomes_decl_Rules.put(3, _i_becomes_decl3);
        _i_becomes_decl_Rules.put(4, _i_becomes_decl4);
        _i_becomes_decl_Rules.put(5, _i_becomes_decl5);
        _i_becomes_decl_Rules.put(6, _i_becomes_decl6);
        _i_becomes_decl_Rules.put(7, _i_becomes_decl7);
        _i_becomes_decl_Rules.put(8, _i_becomes_decl8);
        _i_becomes_decl_Rules.put(9, _i_becomes_decl9);
        _i_becomes_decl_Rules.put(10, _i_becomes_decl10);
        _i_becomes_decl_Rules.put(11, _i_becomes_decl11);
        _i_becomes_decl_Rules.put(12, _i_becomes_decl12);

        startFact = _i_becomes_decl;
    }

    public ISymbolFactory getStartSymbol() {
        return startFact;
    }
}
