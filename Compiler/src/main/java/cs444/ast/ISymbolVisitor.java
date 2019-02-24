package cs444.ast;

import cs444.CompilerException;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.parser.symbols.ast.expressions.*;

public interface ISymbolVisitor {
    void prepare(MethodInvokeSymbol invode) throws CompilerException;

    void prepare(FieldAccessSymbol fieldAccessSymbol) throws CompilerException;

    void prepareCondition(Typeable condition) throws CompilerException;

    void open(AInterfaceOrClassSymbol aInterfaceOrClassSymbol) throws CompilerException;

    void open(DclSymbol dclSymbol) throws CompilerException;

    void open(MethodOrConstructorSymbol method) throws CompilerException;

    void open(CreationExpression creationExpression) throws CompilerException;

    void open(NonTerminal aNonTerminal) throws CompilerException;

    void open(MethodInvokeSymbol invoke) throws CompilerException;

    void open(FieldAccessSymbol field) throws CompilerException;

    void open(WhileExprSymbol whileExprSymbol) throws CompilerException;

    void open(ForExprSymbol forExprSymbol) throws CompilerException;

    void open(IfExprSymbol ifExprSymbol) throws CompilerException;

    void open(ReturnExprSymbol retSymbol) throws CompilerException;

    void close(AInterfaceOrClassSymbol aInterfaceOrClassSymbol) throws CompilerException;

    void close(DclSymbol dclSymbol) throws CompilerException;

    void close(MethodOrConstructorSymbol method) throws CompilerException;

    void close(NonTerminal aNonTerminal) throws CompilerException;

    void close(CreationExpression creationExpression) throws CompilerException;

    void close(MethodInvokeSymbol invoke) throws CompilerException;

    void close(FieldAccessSymbol field) throws CompilerException;

    void close(WhileExprSymbol whileExprSymbol) throws CompilerException;

    void close(ForExprSymbol forExprSymbol) throws CompilerException;

    void close(IfExprSymbol ifExprSymbol) throws CompilerException;

    void close(ReturnExprSymbol returnSymbol) throws CompilerException;

    void prepareElseBody(IfExprSymbol ifExpr) throws CompilerException;

    void afterClause(ForExprSymbol forExprSymbol) throws CompilerException;

    void afterCondition(ForExprSymbol forExprSymbol) throws CompilerException;

    void visit(TypeSymbol typeSymbol) throws CompilerException;

    void visit(NameSymbol nameSymbol) throws CompilerException;

    void visit(ATerminal terminal) throws CompilerException;

    void visit(CastExpressionSymbol symbol) throws CompilerException;

    void visit(NegOpExprSymbol op) throws CompilerException;

    void visit(NotOpExprSymbol op) throws CompilerException;

    void visit(MultiplyExprSymbol op) throws CompilerException;

    void visit(AssignmentExprSymbol op) throws CompilerException;

    void visit(DivideExprSymbol op) throws CompilerException;

    void visit(RemainderExprSymbol op) throws CompilerException;

    void visit(AddExprSymbol op) throws CompilerException;

    void visit(SubtractExprSymbol op) throws CompilerException;

    void visit(LSExprSymbol op) throws CompilerException;

    void visit(RSExprSymbol op) throws CompilerException;

    void visit(URSExprSymbol op) throws CompilerException;

    void visit(LtExprSymbol op) throws CompilerException;

    void visit(GtExprSymbol op) throws CompilerException;

    void visit(EqExprSymbol op) throws CompilerException;

    void visit(NeExprSymbol op) throws CompilerException;

    void visit(AndExprSymbol op) throws CompilerException;

    void visit(OrExprSymbol op) throws CompilerException;

    void visit(EAndExprSymbol op) throws CompilerException;

    void visit(EOrExprSymbol op) throws CompilerException;

    void visit(LeExprSymbol op) throws CompilerException;

    void visit(GeExprSymbol op) throws CompilerException;

    void visit(InstanceOfExprSymbol op) throws CompilerException;

    void visit(IntegerLiteralSymbol intLiteral) throws CompilerException;

    void visit(LongLiteralSymbol longLiteral) throws CompilerException;

    void visit(NullSymbol nullSymbol) throws CompilerException;

    void visit(BooleanLiteralSymbol boolSymbol) throws CompilerException;

    void visit(ThisSymbol thisSymbol) throws CompilerException;

    void visit(SuperSymbol thisSymbol) throws CompilerException;

    void visit(StringLiteralSymbol stringSymbol) throws CompilerException;

    void visit(CharacterLiteralSymbol characterSymbol) throws CompilerException;

    void visit(EmptyStatementSymbol emptySymbol) throws CompilerException;

    void visit(ArrayAccessExprSymbol arrayAccess) throws CompilerException;

    void visit(ShortLiteralSymbol shortLiteral) throws CompilerException;

    void visit(ByteLiteralSymbol byteLiteral) throws CompilerException;

    void visit(SimpleNameSymbol name) throws CompilerException;

    void visit(SimpleMethodInvoke invoke) throws CompilerException;

    void visit(ISymbol symbol) throws CompilerException;

    void middle(MethodOrConstructorSymbol methodOrConstructorSymbol) throws CompilerException;
}
