package cs444.ast;

import cs444.CompilerException;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.ConstructorSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;

public class EmptyVisitor implements ISymbolVisitor {

    @Override
    public void visit(TypeSymbol typeSymbol) throws CompilerException {}

    @Override
    public void visit(ISymbol symbol) throws CompilerException {}

    @Override
    public void open(AInterfaceOrClassSymbol aInterfaceOrClassSymbol)  throws CompilerException {}

    @Override
    public void open(MethodSymbol method)  throws CompilerException  {}

    @Override
    public void open(DclSymbol dclSymbol) throws CompilerException  {}

    @Override
    public void open(ConstructorSymbol constructorSymbol) throws CompilerException {}

    @Override
    public void open(JoosNonTerminal aNonTerminal) throws CompilerException {}

    @Override
    public void close(AInterfaceOrClassSymbol aInterfaceOrClassSymbol) throws CompilerException {}

    @Override
    public void close(MethodSymbol method) throws CompilerException {}

    @Override
    public void close(DclSymbol dclSymbol) throws CompilerException {}

    @Override
    public void close(ConstructorSymbol constructorSymbol) throws CompilerException {}

    @Override
    public void close(JoosNonTerminal aNonTerminal) throws CompilerException {}

    @Override
    public void visit(ATerminal terminal) throws CompilerException {}

    @Override
    public void visit(NameSymbol nameSymbol) throws CompilerException {}
}
