package cs444.ast;

import cs444.CompilerException;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.ConstructorSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.TypeSymbol;

public interface ISymbolVisitor {
    void open(final AInterfaceOrClassSymbol aInterfaceOrClassSymbol) throws CompilerException;
    void open(final MethodSymbol method) throws CompilerException;
    void open(final DclSymbol dclSymbol) throws CompilerException;
    void open(final ConstructorSymbol constructorSymbol) throws CompilerException;
    void open(final JoosNonTerminal aNonTerminal) throws CompilerException;

    void close(final AInterfaceOrClassSymbol aInterfaceOrClassSymbol) throws CompilerException;
    void close(final MethodSymbol method) throws CompilerException;
    void close(final DclSymbol dclSymbol) throws CompilerException;
    void close(final ConstructorSymbol constructorSymbol) throws CompilerException;
    void close(final JoosNonTerminal aNonTerminal) throws CompilerException;

    void visit(final TypeSymbol typeSymbol) throws CompilerException;

    void visit(final ATerminal terminal) throws CompilerException;
    void visit(final ISymbol symbol) throws CompilerException;
}
