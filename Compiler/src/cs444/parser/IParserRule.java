package cs444.parser;

import java.util.Map;

import cs444.parser.symbols.SymbolState;
import cs444.parser.symbols.factories.NonTerminalFactory;

public interface IParserRule {
    Map<Integer, Map<String, SymbolState>> getRules();
    NonTerminalFactory getStartRule();
}
