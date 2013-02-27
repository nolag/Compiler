package cs444.ast;

import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.ConstructorSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.TypeSymbol;

public interface ISymbolVisitor {
    void open(final AInterfaceOrClassSymbol aInterfaceOrClassSymbol);
    void open(final MethodSymbol method);
    void open(final DclSymbol dclSymbol);
    void open(final ConstructorSymbol constructorSymbol);

    void close(final AInterfaceOrClassSymbol aInterfaceOrClassSymbol);
    void close(final MethodSymbol method);
    void close(final DclSymbol dclSymbol);
    void close(final ConstructorSymbol constructorSymbol);

    void visit(final TypeSymbol typeSymbol);

    void visit(final ATerminal terminal);
    void visit(final ISymbol symbol);
}
