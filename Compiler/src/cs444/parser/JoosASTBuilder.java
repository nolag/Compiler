package cs444.parser;

import java.util.Arrays;

import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.ast.factories.ListedSymbolFactory;
import cs444.parser.symbols.ast.factories.OneChildFactory;

public class JoosASTBuilder implements IASTBuilder{

    private static final Iterable<ASTSymbolFactory> simplifications =
            Arrays.asList(new ASTSymbolFactory [] { new ListedSymbolFactory(), new OneChildFactory()});

    public Iterable<ASTSymbolFactory> getSimplifcations() {
        return simplifications;
    }
}
