package cs444.ast;

import cs444.CompilerException;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.expressions.*;

public class EmptyVisitor implements ISymbolVisitor {

    @Override
    public void visit(TypeSymbol typeSymbol) throws CompilerException {}

    @Override
    public void visit(ISymbol symbol) throws CompilerException {}

    @Override
    public void open(AInterfaceOrClassSymbol aInterfaceOrClassSymbol)  throws CompilerException {}

    @Override
    public void open(DclSymbol dclSymbol) throws CompilerException  {}

    @Override
    public void open(MethodOrConstructorSymbol constructorSymbol) throws CompilerException {}

    @Override
    public void open(NonTerminal aNonTerminal) throws CompilerException {}

    @Override
    public void close(AInterfaceOrClassSymbol aInterfaceOrClassSymbol) throws CompilerException {}

    @Override
    public void close(DclSymbol dclSymbol) throws CompilerException {}

    @Override
    public void close(MethodOrConstructorSymbol constructorSymbol) throws CompilerException {}

    @Override
    public void close(NonTerminal aNonTerminal) throws CompilerException {}

    @Override
    public void visit(ATerminal terminal) throws CompilerException {}

    @Override
    public void visit(NameSymbol nameSymbol) throws CompilerException {}

    @Override
    public void open(CreationExpression creationExpression) throws CompilerException {}

    @Override
    public void close(CreationExpression creationExpression) throws CompilerException {}

    @Override
    public void visit(NegOpExprSymbol op) throws CompilerException { }

    @Override
    public void visit(NotOpExprSymbol op) throws CompilerException { }

    @Override
    public void visit(MultiplyExprSymbol op) throws CompilerException { }

    @Override
    public void visit(AssignmentExprSymbol op) throws CompilerException { }

    @Override
    public void visit(DivideExprSymbol op) throws CompilerException { }

    @Override
    public void visit(RemainderExprSymbol op) throws CompilerException { }

    @Override
    public void visit(AddExprSymbol op) throws CompilerException { }

    @Override
    public void visit(SubtractExprSymbol op) throws CompilerException { }

    @Override
    public void visit(LtExprSymbol op) throws CompilerException { }

    @Override
    public void visit(EqExprSymbol op) throws CompilerException { }

    @Override
    public void visit(NeExprSymbol op) throws CompilerException { }

    @Override
    public void visit(AndExprSymbol op) throws CompilerException { }

    @Override
    public void visit(OrExprSymbol op) throws CompilerException { }

    @Override
    public void visit(LeExprSymbol op) throws CompilerException { }

    @Override
    public void visit(InstanceOfExprSymbol op) throws CompilerException { }

    @Override
    public void visit(CastExpressionSymbol symbol) throws CompilerException { }

    @Override
    public void visit(EAndExprSymbol op) throws CompilerException { }

    @Override
    public void visit(EOrExprSymbol op) throws CompilerException { }

    @Override
    public void open(MethodInvokeSymbol invoke) throws CompilerException { }

    @Override
    public void close(MethodInvokeSymbol invoke) throws CompilerException { }

    @Override
    public void prepare(MethodInvokeSymbol invode) throws CompilerException { }

    @Override
    public void visit(IntegerLiteralSymbol intLiteral) throws CompilerException { }

    @Override
    public void open(FieldAccessSymbol field) throws CompilerException { }

    @Override
    public void close(FieldAccessSymbol field) throws CompilerException { }

    @Override
    public void prepareCondition(Typeable condition) { }

    @Override
    public void open(WhileExprSymbol whileExprSymbol) { }

    @Override
    public void close(WhileExprSymbol whileExprSymbol) throws CompilerException { }

    @Override
    public void visit(NullSymbol nullSymbol) throws CompilerException { }

    @Override
    public void visit(BooleanLiteralSymbol boolSymbol) throws CompilerException { }

    @Override
    public void visit(ThisSymbol thisSymbol) throws CompilerException { }

    @Override
    public void visit(SuperSymbol thisSymbol) throws CompilerException { }

    @Override
    public void visit(StringLiteralSymbol stringSymbol) throws CompilerException { }

    @Override
    public void visit(CharacterLiteralSymbol characterSymbol) throws CompilerException { }

    @Override
    public void visit(ReturnExprSymbol returnSymbol) throws CompilerException { }

    @Override
    public void open(ForExprSymbol forExprSymbol) throws CompilerException { }

    @Override
    public void close(ForExprSymbol forExprSymbol) throws CompilerException { }

    @Override
    public void open(IfExprSymbol ifExprSymbol) throws CompilerException { }

    @Override
    public void close(IfExprSymbol ifExprSymbol) throws CompilerException { }
}
