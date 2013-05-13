package cs444.static_analysis;

import java.util.Stack;

import cs444.CompilerException;
import cs444.ast.EmptyVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.EmptyStatementSymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.parser.symbols.ast.expressions.ForExprSymbol;
import cs444.parser.symbols.ast.expressions.IfExprSymbol;
import cs444.parser.symbols.ast.expressions.ReturnExprSymbol;
import cs444.parser.symbols.ast.expressions.WhileExprSymbol;
import cs444.static_analysis.exceptions.MissingReturnStatement;
import cs444.static_analysis.exceptions.UnreachableCode;
import cs444.types.ContextInfo;
import cs444.types.exceptions.UndeclaredException;

public class ReachabilityAnalyzer extends EmptyVisitor {
    private final ContextInfo context;
    private final boolean MAYBE = true;
    private final boolean NO = false;

    private final Stack<Boolean> stack = new Stack<Boolean>();

    public ReachabilityAnalyzer(String enclosingClassName){
        context = new ContextInfo(enclosingClassName);
    }

    @Override
    public void open(MethodOrConstructorSymbol method) throws CompilerException {
        context.setCurrentMember(method);
        stack.push(MAYBE);
    }

    @Override
    public void close(MethodOrConstructorSymbol method)
            throws CompilerException {
        boolean outMethod = stack.pop();
        MethodOrConstructorSymbol currentMC = (MethodOrConstructorSymbol) context.getCurrentMember();
        if (outMethod == MAYBE && !currentMC.isVoid() && !currentMC.isAbstract() && !currentMC.isNative()){
            throw new MissingReturnStatement(context.enclosingClassName, context.getMemberName());
        }
        context.setCurrentMember(null);
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
        if(stack.peek() == NO) throw new UnreachableCode(what, context.enclosingClassName, context.getMemberName());
    }
    @Override
    public void open(WhileExprSymbol whileExprSymbol) throws CompilerException {
        assertIsReachable(whileExprSymbol.getName());

        Typeable condition = whileExprSymbol.getCondition();
        analyzeLoopConditionEntry(condition);
    }

    @Override
    public void close(WhileExprSymbol whileExprSymbol) throws CompilerException {
        boolean outWhileBody = stack.pop();
        boolean inWhileExpr = stack.pop();

        Typeable condition = whileExprSymbol.getCondition();
        analyzeLoopConditionExit(condition, inWhileExpr, outWhileBody);
    }

    @Override
    public void open(ForExprSymbol forExprSymbol) throws CompilerException {
        assertIsReachable(forExprSymbol.getName());

        analyzeLoopConditionEntry(forExprSymbol.getCondition());
    }

    @Override
    public void close(ForExprSymbol forExprSymbol) throws CompilerException {
        boolean outForBody = stack.pop();
        boolean inForExpr = stack.pop();
        analyzeLoopConditionExit(forExprSymbol.getCondition(), inForExpr, outForBody);
    }

    private void analyzeLoopConditionEntry(Typeable condition) {
        if (conditionEvalTo(condition, false)){
            stack.push(NO);
        }else{
            stack.push(stack.peek());
        }
    }

    private void analyzeLoopConditionExit(Typeable condition,
            boolean inLoop, boolean outBody) {
        if (conditionEvalTo(condition, true)){
            stack.push(NO);
        }else{
            stack.push(outBody || inLoop);
        }
    }

    private boolean conditionEvalTo(Typeable condition, boolean to){
        return condition instanceof BooleanLiteralSymbol &&
                ((BooleanLiteralSymbol) condition).boolValue == to;
    }
}
