package cs444.ast;

import cs444.CompilerException;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.*;

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
    public void open(JoosNonTerminal aNonTerminal){
        level++;
    }

    @Override
    public void close(AInterfaceOrClassSymbol aInterfaceOrClassSymbol) {
        level--;
    }

    @Override
    public void close(MethodSymbol method) {
        level--;
    }

    @Override
    public void close(DclSymbol dclSymbol) {
        level--;
    }

    @Override
    public void close(MethodOrConstructorSymbol methodSymbol) {
        level--;
    }

    @Override
    public void close(JoosNonTerminal aNonTerminal){
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
}
