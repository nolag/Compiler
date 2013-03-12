package cs444.ast;

import cs444.CompilerException;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
import cs444.parser.symbols.ast.DclSymbol;
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
import cs444.parser.symbols.ast.expressions.ReturnExprSymbol;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;
import cs444.parser.symbols.ast.expressions.WhileExprSymbol;

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
            {
        print("new");
        level++;
    }

    @Override
    public void close(CreationExpression creationExpression)
            {
        level--;
    }

    @Override
    public void visit(TypeSymbol typeSymbol) {
        // dclSymbol is printing its type
    }

    @Override
    public void visit(NameSymbol nameSymbol){
        print("Name Symbol: " + nameSymbol.value);
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
    public void visit(NegOpExprSymbol op) {
        System.out.printf("%" + (2*level + 1) + "s\n", "-");
    }

    @Override
    public void visit(NotOpExprSymbol op){
        System.out.printf("%" + (2*level + 1) + "s\n", "!");
    }

    @Override
    public void visit(MultiplyExprSymbol op){
        System.out.printf("%" + (2*level + 1) + "s\n", "*");
    }

    @Override
    public void visit(AssignmentExprSymbol op){
        System.out.printf("%" + (2*level + 1) + "s\n", "=");
    }

    @Override
    public void visit(DivideExprSymbol op){
        System.out.printf("%" + (2*level + 1) + "s\n", "//");
    }

    @Override
    public void visit(RemainderExprSymbol op) {
        System.out.printf("%" + (2*level + 1) + "s\n", "%");
    }

    @Override
    public void visit(AddExprSymbol op){
        System.out.printf("%" + (2*level + 1) + "s\n", "+");
    }

    @Override
    public void visit(SubtractExprSymbol op) {
        System.out.printf("%" + (2*level + 1) + "s\n", "-");
    }

    @Override
    public void visit(LtExprSymbol op) {
        System.out.printf("%" + (2*level + 1) + "s\n", "<");
    }

    @Override
    public void visit(EqExprSymbol op) {
        System.out.printf("%" + (2*level + 2) + "s\n", "==");
    }

    @Override
    public void visit(NeExprSymbol op) {
        System.out.printf("%" + (2*level + 2) + "s\n", "!=");
    }

    @Override
    public void visit(AndExprSymbol op) {
        System.out.printf("%" + (2*level + 2) + "s\n", "&&");
    }

    @Override
    public void visit(OrExprSymbol op) {
        System.out.printf("%" + (2*level + 2) + "s\n", "||");
    }

    @Override
    public void visit(LeExprSymbol op) {
        System.out.printf("%" + (2*level + 2) + "s\n", "<=");
    }

    @Override
    public void visit(InstanceOfExprSymbol op) {
        System.out.printf("%" + (2*level + "instanceof".length()) + "s\n", "instanceof");
    }

    @Override
    public void visit(CastExpressionSymbol symbol) {
        System.out.printf("%" + (2*level + 4) + "s\n", "CAST");
    }

    @Override
    public void visit(EAndExprSymbol op) {
        System.out.printf("%" + (2*level + 1) + "s\n", "&");
    }

    @Override
    public void visit(EOrExprSymbol op) {
        System.out.printf("%" + (2*level + 1) + "s\n", "|");
    }

    @Override
    public void open(MethodInvokeSymbol invoke) {
        print("method call: " + invoke.methodName);
        level++;
    }

    @Override
    public void close(MethodInvokeSymbol invoke) {
        level--;
    }

    @Override
    public void prepare(MethodInvokeSymbol invode) throws CompilerException { }

    public void visit(IntegerLiteralSymbol intLiteral) throws CompilerException {
        print("IntegerLiteralSymbol: " + intLiteral.value);
    }

    @Override
    public void open(FieldAccessSymbol field) throws CompilerException {
        print("fieldAccess: ");
        level++;
    }

    @Override
    public void close(FieldAccessSymbol field) throws CompilerException{
        level--;
    }

    @Override
    public void prepareCondition(Typeable condition) {
        print("condition :");

    }

    @Override
    public void open(WhileExprSymbol whileExprSymbol) {
        print("while");
        level++;
    }

    @Override
    public void close(WhileExprSymbol whileExprSymbol) throws CompilerException {
        level--;
    }

    @Override
    public void visit(NullSymbol nullSymbol) throws CompilerException {
        print("null");
    }

    @Override
    public void visit(BooleanLiteralSymbol boolSymbol) throws CompilerException {
        print("boolean: " + boolSymbol.boolValue);
    }

    @Override
    public void visit(ThisSymbol thisSymbol) throws CompilerException {
        print("this");
    }

    @Override
    public void visit(SuperSymbol superSymbol) throws CompilerException {
        print("super");
    }

    @Override
    public void visit(StringLiteralSymbol stringSymbol) throws CompilerException {
        print("Stiring: " + stringSymbol.strValue);
    }

    @Override
    public void visit(CharacterLiteralSymbol characterSymbol) throws CompilerException {
        print("Character: " + characterSymbol.charVal);
    }

    @Override
    public void visit(ReturnExprSymbol returnSymbol) throws CompilerException {
        print("Return :");
    }
}
