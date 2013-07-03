package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;

public class ListedSymbol extends ANonTerminal{

    public ListedSymbol(ANonTerminal from, int child){
        super(from.getName());
        ISymbol symbol = from;
        while(symbol.getName().equals(name)){
            if(from.children.size() == 1){
                if(child == 1)children.add(from.children.get(0));
                else children.add(0, from.children.get(0));
                return;
            }

            if(child == 1) children.add(from.children.get(0));
            else children.add(0, from.children.get(1));

            symbol = from.children.get(child);
            if(ANonTerminal.class.isInstance(symbol)) from = (NonTerminal) symbol;
            else{
                children.add(symbol);
                break;
            }
        }
    }

    @Override
    public boolean isCollapsable() {
        return true;
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        for (ISymbol child : children) {
            child.accept(visitor);
        }
    }

    @Override
    public void accept(CodeGenVisitor visitor) {
        visitor.visit(this);
    }
}
