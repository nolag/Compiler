package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;

public class OneChildFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ANonTerminal from) {
        return from.children.size() == 1 ? from.children.get(0) : from;
    }
}
