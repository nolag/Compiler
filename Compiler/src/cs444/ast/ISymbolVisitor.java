package cs444.ast;

import cs444.CompilerException;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;
import cs444.parser.symbols.ast.expressions.AndExprSymbol;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;
import cs444.parser.symbols.ast.expressions.EAndExprSymbol;
import cs444.parser.symbols.ast.expressions.EOrExprSymbol;
import cs444.parser.symbols.ast.expressions.EqExprSymbol;
import cs444.parser.symbols.ast.expressions.InstanceOfExprSymbol;
import cs444.parser.symbols.ast.expressions.LeExprSymbol;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;
import cs444.parser.symbols.ast.expressions.NeExprSymbol;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;
import cs444.parser.symbols.ast.expressions.NotOpExprSymbol;
import cs444.parser.symbols.ast.expressions.OrExprSymbol;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;
import cs444.parser.symbols.ast.expressions.WhileExprSymbol;

public interface ISymbolVisitor {
    void prepare(final MethodInvokeSymbol invode) throws CompilerException;
    void prepareCondition(Typeable condition);

    void open(final AInterfaceOrClassSymbol aInterfaceOrClassSymbol) throws CompilerException;
    void open(final DclSymbol dclSymbol) throws CompilerException;
    void open(final MethodOrConstructorSymbol method) throws CompilerException;
    void open(final CreationExpression creationExpression) throws CompilerException;
    void open(final NonTerminal aNonTerminal) throws CompilerException;
    void open(final MethodInvokeSymbol invoke) throws CompilerException;
    void open(final FieldAccessSymbol field) throws CompilerException;
    void open(WhileExprSymbol whileExprSymbol);

    void close(final AInterfaceOrClassSymbol aInterfaceOrClassSymbol) throws CompilerException;
    void close(final DclSymbol dclSymbol) throws CompilerException;
    void close(final MethodOrConstructorSymbol method) throws CompilerException;
    void close(final NonTerminal aNonTerminal) throws CompilerException;
    void close(final CreationExpression creationExpression) throws CompilerException;
    void close(final MethodInvokeSymbol invoke) throws CompilerException;
    void close(final FieldAccessSymbol field) throws CompilerException;
    void close(final WhileExprSymbol whileExprSymbol) throws CompilerException;

    void visit(final TypeSymbol typeSymbol) throws CompilerException;
    void visit(final NameSymbol nameSymbol) throws CompilerException;
    void visit(final ATerminal terminal) throws CompilerException;

    void visit(CastExpressionSymbol symbol) throws CompilerException;

    void visit(final NegOpExprSymbol op) throws CompilerException;
    void visit(final NotOpExprSymbol op) throws CompilerException;
    void visit(final MultiplyExprSymbol op) throws CompilerException;
    void visit(final AssignmentExprSymbol op) throws CompilerException;
    void visit(final DivideExprSymbol op) throws CompilerException;
    void visit(final RemainderExprSymbol op) throws CompilerException;
    void visit(final AddExprSymbol op)  throws CompilerException;
    void visit(final SubtractExprSymbol op) throws CompilerException;
    void visit(final LtExprSymbol op) throws CompilerException;
    void visit(final EqExprSymbol op) throws CompilerException;
    void visit(final NeExprSymbol op) throws CompilerException;
    void visit(final AndExprSymbol op) throws CompilerException;
    void visit(final OrExprSymbol op) throws CompilerException;
    void visit(final EAndExprSymbol op) throws CompilerException;
    void visit(final EOrExprSymbol op) throws CompilerException;
    void visit(final LeExprSymbol op) throws CompilerException;
    void visit(final InstanceOfExprSymbol op) throws CompilerException;
    void visit(final IntegerLiteralSymbol intLiteral) throws CompilerException;

    void visit(final ISymbol symbol) throws CompilerException;
}
