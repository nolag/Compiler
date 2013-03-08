package cs444.ast;

import cs444.CompilerException;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.DclSymbol;
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

public class PrettyPrinter implements ISymbolVisitor {

    int level = 0;

    @Override
    public void open(AInterfaceOrClassSymbol aInterfaceOrClassSymbol) {
        print("InterfOrClass: " + aInterfaceOrClassSymbol.dclName);
        level++;
    }

    @Override
    public void open(DclSymbol dclSymbol) {
        print("dclSymbol: " + dclSymbol.type.value + " " + dclSymbol.dclName);
        level++;
    }

    @Override
    public void open(MethodOrConstructorSymbol method) {
        if (method.type == null){
            print("Constructor: " + method.dclName);
        }else{
            print("Method: " + method.type.value + " " + method.dclName);
        }
        level++;
    }

    @Override
    public void open(NonTerminal aNonTerminal){
        level++;
    }

    @Override
    public void close(AInterfaceOrClassSymbol aInterfaceOrClassSymbol) {
        level--;
    }

    @Override
    public void close(MethodOrConstructorSymbol method) {
        level--;
    }

    @Override
    public void close(DclSymbol dclSymbol) {
        level--;
    }

    @Override
    public void close(NonTerminal aNonTerminal){
        level--;
    }

    @Override
    public void open(CreationExpression creationExpression)
            throws CompilerException {
        print("new");
        level++;
    }

    @Override
    public void close(CreationExpression creationExpression)
            throws CompilerException {
        level--;
    }

    @Override
    public void visit(TypeSymbol typeSymbol) {
        // dclSymbol is printing its type
    }

    @Override
    public void visit(NameSymbol nameSymbol) throws CompilerException {
        print("symbol: " + nameSymbol.value);
    }

    @Override
    public void visit(ATerminal terminal) {
        print(terminal.getName() + ": " + terminal.value);
    }

    @Override
    public void visit(ISymbol symbol) {
        print(symbol.getName());
    }

    private void print(String str) {
        System.out.printf("%" + (2*level + str.length()) + "s\n", str);
    }

    @Override
    public void visit(NegOpExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 1) + "s\n", "-");
    }

    @Override
    public void visit(NotOpExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 1) + "s\n", "!");
    }

    @Override
    public void visit(MultiplyExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 1) + "s\n", "*");
    }

    @Override
    public void visit(AssignmentExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 1) + "s\n", "=");
    }

    @Override
    public void visit(DivideExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 1) + "s\n", "//");
    }

    @Override
    public void visit(RemainderExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 1) + "s\n", "%");
    }

    @Override
    public void visit(AddExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 1) + "s\n", "+");
    }

    @Override
    public void visit(SubtractExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 1) + "s\n", "-");
    }

    @Override
    public void visit(LtExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 1) + "s\n", "<");
    }

    @Override
    public void visit(EqExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 2) + "s\n", "==");
    }

    @Override
    public void visit(NeExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 2) + "s\n", "!=");
    }

    @Override
    public void visit(AndExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 2) + "s\n", "&&");
    }

    @Override
    public void visit(OrExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 2) + "s\n", "||");
    }

    @Override
    public void visit(LeExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 2) + "s\n", "<=");
    }

    @Override
    public void visit(InstanceOfExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + "instanceof".length()) + "s\n", "instanceof");
    }

    @Override
    public void visit(CastExpressionSymbol symbol) throws CompilerException {
        System.out.printf("%" + (2*level + 4) + "s\n", "CAST");
    }

    @Override
    public void visit(EAndExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 1) + "s\n", "&");
    }

    @Override
    public void visit(EOrExprSymbol op) throws CompilerException {
        System.out.printf("%" + (2*level + 1) + "s\n", "|");
    }
}
