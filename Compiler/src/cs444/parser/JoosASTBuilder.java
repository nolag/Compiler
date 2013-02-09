package cs444.parser;

import java.util.Arrays;

import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.ast.factories.ClassInterfaceFactory;
import cs444.parser.symbols.ast.factories.IntegerLiteralFactory;
import cs444.parser.symbols.ast.factories.ListedSymbolFactory;
import cs444.parser.symbols.ast.factories.OneChildFactory;
import cs444.parser.symbols.ast.factories.TypeSymbolFactory;
import cs444.parser.symbols.ast.factories.StringLiteralFactory;

public class JoosASTBuilder implements IASTBuilder{
    private static final OneChildFactory oneChild =  new OneChildFactory();
    private static final Iterable<ASTSymbolFactory> simplifications = Arrays.asList(new ASTSymbolFactory [] {
            new ListedSymbolFactory(), new TypeSymbolFactory(), new ClassInterfaceFactory(), new IntegerLiteralFactory(), new StringLiteralFactory(), oneChild});

    public Iterable<ASTSymbolFactory> getSimplifcations() {
        return simplifications;
    }

}
