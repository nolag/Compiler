package cs444.types;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
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

public class TypeCheckerVisitor implements ISymbolVisitor {

    private PkgClassResolver pkgClassResolver;
    private TypeSymbol currentMethodType;

    public TypeCheckerVisitor(PkgClassResolver pkgClassResolver){
        this.pkgClassResolver = pkgClassResolver;
    }

    @Override
    public void open(AInterfaceOrClassSymbol aInterfaceOrClassSymbol)
            throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void open(DclSymbol dclSymbol) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void open(MethodOrConstructorSymbol method) throws CompilerException {
        this.currentMethodType = method.type;
    }

    @Override
    public void open(CreationExpression creationExpression)
            throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void open(NonTerminal aNonTerminal) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void close(AInterfaceOrClassSymbol aInterfaceOrClassSymbol)
            throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void close(DclSymbol dclSymbol) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void close(MethodOrConstructorSymbol method)
            throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void close(NonTerminal aNonTerminal) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void close(CreationExpression creationExpression)
            throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(TypeSymbol typeSymbol) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NameSymbol nameSymbol) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ATerminal terminal) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CastExpressionSymbol symbol) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NegOpExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NotOpExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(MultiplyExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(AssignmentExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(DivideExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(RemainderExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(AddExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(SubtractExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(LtExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(EqExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NeExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(AndExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(OrExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(EAndExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(EOrExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(LeExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(InstanceOfExprSymbol op) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ISymbol symbol) throws CompilerException {
        // TODO Auto-generated method stub

    }

    @Override
    public void prepare(MethodInvokeSymbol invode) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void open(MethodInvokeSymbol invoke) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void close(MethodInvokeSymbol invoke) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(IntegerLiteralSymbol intLiteral) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

}
