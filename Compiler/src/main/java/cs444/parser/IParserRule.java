package cs444.parser;

import cs444.parser.symbols.SymbolState;

import java.io.IOException;
import java.util.Map;

public interface IParserRule {
    Map<Integer, Map<String, SymbolState>> getRules() throws IOException;
}
