package cs444.ast;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.ConstructorSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.TypeSymbol;

public class EmptyVisitor implements ISymbolVisitor {

    @Override
    public void visit(TypeSymbol typeSymbol) {}

    @Override
    public void visit(ISymbol symbol) {}

    @Override
    public void open(AInterfaceOrClassSymbol aInterfaceOrClassSymbol) {}

    @Override
    public void open(MethodSymbol method) {}

    @Override
    public void open(DclSymbol dclSymbol) {}

    @Override
    public void open(ConstructorSymbol constructorSymbol) {}

    @Override
    public void close(AInterfaceOrClassSymbol aInterfaceOrClassSymbol) {}

    @Override
    public void close(MethodSymbol method) {}

    @Override
    public void close(DclSymbol dclSymbol) {}

    @Override
    public void close(ConstructorSymbol constructorSymbol) {}

}
