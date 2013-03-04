package cs444.ast;

import cs444.CompilerException;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.CreationExpression;

public interface ISymbolVisitor {
    void open(final AInterfaceOrClassSymbol aInterfaceOrClassSymbol) throws CompilerException;
    void open(final DclSymbol dclSymbol) throws CompilerException;
    void open(final MethodOrConstructorSymbol method) throws CompilerException;
    void open(final CreationExpression creationExpression) throws CompilerException;
    void open(final JoosNonTerminal aNonTerminal) throws CompilerException;

    void close(final AInterfaceOrClassSymbol aInterfaceOrClassSymbol) throws CompilerException;
    void close(final MethodSymbol method) throws CompilerException;
    void close(final DclSymbol dclSymbol) throws CompilerException;
    void close(final MethodOrConstructorSymbol method) throws CompilerException;
    void close(final JoosNonTerminal aNonTerminal) throws CompilerException;
    void close(final CreationExpression creationExpression) throws CompilerException;

    void visit(final TypeSymbol typeSymbol) throws CompilerException;
    void visit(final NameSymbol nameSymbol) throws CompilerException;
    void visit(final ATerminal terminal) throws CompilerException;
    void visit(final ISymbol symbol) throws CompilerException;
}
