package cs444.parser;

import java.util.Map;

import cs444.parser.symbols.SymbolState;

public interface IParserRule {
    Map<Integer, Map<String, SymbolState>> getRules();
}
