package cs444.static_analysis;

import java.util.Stack;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.EmptyStatementSymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.NullSymbol;
import cs444.parser.symbols.ast.StringLiteralSymbol;
import cs444.parser.symbols.ast.SuperSymbol;
import cs444.parser.symbols.ast.ThisSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;
import cs444.parser.symbols.ast.expressions.AndExprSymbol;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;
import cs444.parser.symbols.ast.expressions.EAndExprSymbol;
import cs444.parser.symbols.ast.expressions.EOrExprSymbol;
import cs444.parser.symbols.ast.expressions.EqExprSymbol;
import cs444.parser.symbols.ast.expressions.ForExprSymbol;
import cs444.parser.symbols.ast.expressions.IfExprSymbol;
import cs444.parser.symbols.ast.expressions.InstanceOfExprSymbol;
import cs444.parser.symbols.ast.expressions.LeExprSymbol;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;
import cs444.parser.symbols.ast.expressions.NeExprSymbol;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;
import cs444.parser.symbols.ast.expressions.NotOpExprSymbol;
import cs444.parser.symbols.ast.expressions.OrExprSymbol;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;
import cs444.parser.symbols.ast.expressions.ReturnExprSymbol;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;
import cs444.parser.symbols.ast.expressions.WhileExprSymbol;
import cs444.static_analysis.exceptions.MissingReturnStatement;
import cs444.static_analysis.exceptions.UnreachableCode;
import cs444.types.ContextInfo;
import cs444.types.exceptions.UndeclaredException;

public class ReachabilityAnalyzer implements ISymbolVisitor {
    private final ContextInfo context;
    private final boolean MAYBE = true;
    private final boolean NO = false;

    private final Stack<Boolean> stack = new Stack<Boolean>();

    public ReachabilityAnalyzer(String enclosingClassName){
        context = new ContextInfo(enclosingClassName);
    }

    @Override
    public void open(MethodOrConstructorSymbol method) throws CompilerException {
        context.setCurrentMC(method);
        stack.push(MAYBE);
    }

    @Override
    public void close(MethodOrConstructorSymbol method)
            throws CompilerException {
        boolean outMethod = stack.pop();
        // TODO: uncomment this after loops are done
//        MethodOrConstructorSymbol currentMC = context.getCurrentMC();
//        if (outMethod == MAYBE && !currentMC.isVoid() && !currentMC.isAbstract() && !currentMC.isNative()){
//            throw new MissingReturnStatement(context.enclosingClassName, context.getMethodName());
//        }
        context.setCurrentMC(null);
    }

    @Override
    public void open(ReturnExprSymbol retSymbol) throws CompilerException {
        assertIsReachable(retSymbol.getName());
    }

    @Override
    public void close(ReturnExprSymbol returnSymbol) throws CompilerException {
        stack.pop();
        stack.push(NO);
    }

    @Override
    public void open(DclSymbol dclSymbol) throws CompilerException {
        assertIsReachable(dclSymbol.dclName);
    }

    @Override
    public void open(IfExprSymbol ifExprSymbol) throws CompilerException {
        assertIsReachable(ifExprSymbol.getName());
        stack.push(stack.peek());   // frame for if body
    }

    @Override
    public void prepareElseBody(IfExprSymbol ifExpr) throws CompilerException {
        boolean outOfIfBody = stack.pop();
        Boolean inOfIfExp = stack.pop();
        stack.push(outOfIfBody);
        stack.push(inOfIfExp);      // frame for else body
    }

    @Override
    public void close(IfExprSymbol ifExprSymbol) throws CompilerException {
        if (ifExprSymbol.getElseBody() == null){
            stack.pop();  // discard out of ifBody
        }else{
            boolean outElseBody = stack.pop();
            boolean outIfBody = stack.pop();
            stack.push(outIfBody || outElseBody);
        }
    }

    @Override
    public void open(NonTerminal aNonTerminal) throws CompilerException {
        assertIsReachable(aNonTerminal.getName());
    }

    @Override
    public void open(CreationExpression creationExpression)
            throws CompilerException {
        assertIsReachable(creationExpression.getName());
    }

    @Override
    public void open(MethodInvokeSymbol invoke) throws CompilerException {
        assertIsReachable(invoke.methodName);
    }

    @Override
    public void open(FieldAccessSymbol field) throws CompilerException {
        assertIsReachable(field.getName());
    }

    @Override
    public void visit(ATerminal terminal) throws CompilerException {
        assertIsReachable(terminal.value);
    }

    @Override
    public void visit(CastExpressionSymbol symbol) throws CompilerException {
        assertIsReachable(symbol.getName());
    }

    @Override
    public void visit(AssignmentExprSymbol op) throws CompilerException {
        assertIsReachable(op.getName());
    }

    @Override
    public void visit(EmptyStatementSymbol emptySymbol)
            throws CompilerException {
        assertIsReachable(emptySymbol.getName());
    }

    @Override
    public void visit(ISymbol symbol) throws CompilerException {
        assertIsReachable(symbol.getName());
    }

    private void assertIsReachable(String what) throws UnreachableCode, UndeclaredException {
        if(stack.peek() == NO) throw new UnreachableCode(what, context.enclosingClassName, context.getMethodName());
    }

    @Override
    public void prepare(MethodInvokeSymbol invode) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void prepare(FieldAccessSymbol fieldAccessSymbol)
            throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void prepareCondition(Typeable condition) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void open(AInterfaceOrClassSymbol aInterfaceOrClassSymbol)
            throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void open(WhileExprSymbol whileExprSymbol) throws CompilerException {
        assertIsReachable(whileExprSymbol.getName());
        stack.push(stack.peek());
    }

    @Override
    public void close(WhileExprSymbol whileExprSymbol) throws CompilerException {
        boolean outWhileBody = stack.pop();
        boolean inWhileExpr = stack.pop();

        Typeable whileCond = whileExprSymbol.getCondition();
        if (whileCond instanceof BooleanLiteralSymbol &&
                ((BooleanLiteralSymbol) whileCond).boolValue){
            stack.push(NO);
        }else{
            stack.push(outWhileBody || inWhileExpr);
        }
    }

    @Override
    public void open(ForExprSymbol forExprSymbol) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void close(ForExprSymbol forExprSymbol) throws CompilerException {
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
    public void close(NonTerminal aNonTerminal) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void close(CreationExpression creationExpression)
            throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void close(MethodInvokeSymbol invoke) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void close(FieldAccessSymbol field) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterClause(ForExprSymbol forExprSymbol)
            throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterCondition(ForExprSymbol forExprSymbol)
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
    public void visit(IntegerLiteralSymbol intLiteral) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(NullSymbol nullSymbol) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(BooleanLiteralSymbol boolSymbol) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(ThisSymbol thisSymbol) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(SuperSymbol thisSymbol) throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(StringLiteralSymbol stringSymbol)
            throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(CharacterLiteralSymbol characterSymbol)
            throws CompilerException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(ArrayAccessExprSymbol arrayAccess)
            throws CompilerException {
        // TODO Auto-generated method stub
        
    }
}
