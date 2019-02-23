package cs444.ast;

import cs444.CompilerException;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.parser.symbols.ast.expressions.*;

public abstract class EmptyVisitor implements ISymbolVisitor {

    @Override
    public void visit(final TypeSymbol typeSymbol) throws CompilerException {}

    @Override
    public void visit(final ISymbol symbol) throws CompilerException {}

    @Override
    public void open(final AInterfaceOrClassSymbol aInterfaceOrClassSymbol)  throws CompilerException {}

    @Override
    public void open(final DclSymbol dclSymbol) throws CompilerException  {}

    @Override
    public void open(final MethodOrConstructorSymbol constructorSymbol) throws CompilerException {}

    @Override
    public void open(final NonTerminal aNonTerminal) throws CompilerException {}

    @Override
    public void close(final AInterfaceOrClassSymbol aInterfaceOrClassSymbol) throws CompilerException {}

    @Override
    public void close(final DclSymbol dclSymbol) throws CompilerException {}

    @Override
    public void close(final MethodOrConstructorSymbol constructorSymbol) throws CompilerException {}

    @Override
    public void close(final NonTerminal aNonTerminal) throws CompilerException {}

    @Override
    public void visit(final ATerminal terminal) throws CompilerException {}

    @Override
    public void visit(final NameSymbol nameSymbol) throws CompilerException {}

    @Override
    public void open(final CreationExpression creationExpression) throws CompilerException {}

    @Override
    public void close(final CreationExpression creationExpression) throws CompilerException {}

    @Override
    public void visit(final NegOpExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final NotOpExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final MultiplyExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final AssignmentExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final DivideExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final RemainderExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final AddExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final SubtractExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final LSExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final RSExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final URSExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final LtExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final GtExprSymbol op) throws CompilerException { }
    
    @Override
    public void visit(final EqExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final NeExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final AndExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final OrExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final LeExprSymbol op) throws CompilerException { }
    
    @Override
    public void visit(final GeExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final InstanceOfExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final CastExpressionSymbol symbol) throws CompilerException { }

    @Override
    public void visit(final EAndExprSymbol op) throws CompilerException { }

    @Override
    public void visit(final EOrExprSymbol op) throws CompilerException { }

    @Override
    public void open(final MethodInvokeSymbol invoke) throws CompilerException { }

    @Override
    public void close(final MethodInvokeSymbol invoke) throws CompilerException { }

    @Override
    public void prepare(final MethodInvokeSymbol invode) throws CompilerException { }

    @Override
    public void visit(final IntegerLiteralSymbol intLiteral) throws CompilerException { }

    @Override
    public void visit(final LongLiteralSymbol longLiteral) throws CompilerException { }

    @Override
    public void open(final FieldAccessSymbol field) throws CompilerException { }

    @Override
    public void close(final FieldAccessSymbol field) throws CompilerException { }

    @Override
    public void prepareCondition(final Typeable condition) { }

    @Override
    public void open(final WhileExprSymbol whileExprSymbol) throws CompilerException { }

    @Override
    public void close(final WhileExprSymbol whileExprSymbol) throws CompilerException { }

    @Override
    public void visit(final NullSymbol nullSymbol) throws CompilerException { }

    @Override
    public void visit(final BooleanLiteralSymbol boolSymbol) throws CompilerException { }

    @Override
    public void visit(final ThisSymbol thisSymbol) throws CompilerException { }

    @Override
    public void visit(final SuperSymbol thisSymbol) throws CompilerException { }

    @Override
    public void visit(final StringLiteralSymbol stringSymbol) throws CompilerException { }

    @Override
    public void visit(final CharacterLiteralSymbol characterSymbol) throws CompilerException { }

    @Override
    public void close(final ReturnExprSymbol returnSymbol) throws CompilerException { }

    @Override
    public void open(final ForExprSymbol forExprSymbol) throws CompilerException { }

    @Override
    public void close(final ForExprSymbol forExprSymbol) throws CompilerException { }

    @Override
    public void open(final IfExprSymbol ifExprSymbol) throws CompilerException { }

    @Override
    public void close(final IfExprSymbol ifExprSymbol) throws CompilerException { }

    @Override
    public void open(final ReturnExprSymbol retSymbol) throws CompilerException { }

    @Override
    public void prepare(final FieldAccessSymbol fieldAccessSymbol) throws CompilerException { }

    @Override
    public void visit(final EmptyStatementSymbol emptySymbol) throws CompilerException { }

    @Override
    public void visit(final ArrayAccessExprSymbol arrayAccess) throws CompilerException { }

    @Override
    public void afterClause(final ForExprSymbol forExprSymbol)
            throws CompilerException { }

    @Override
    public void afterCondition(final ForExprSymbol forExprSymbol)
            throws CompilerException { }

    @Override
    public void prepareElseBody(final IfExprSymbol ifExpr) throws CompilerException { }

    @Override
    public void middle(final MethodOrConstructorSymbol methodOrConstructorSymbol) throws CompilerException { }

    @Override
    public void visit(final ShortLiteralSymbol shortLiteral) throws CompilerException { }

    @Override
    public void visit(final ByteLiteralSymbol byteLiteral) throws CompilerException { }

    @Override
    public void visit(final SimpleNameSymbol name) throws CompilerException {	}

    @Override
    public void visit(final SimpleMethodInvoke invoke) throws CompilerException { }
}