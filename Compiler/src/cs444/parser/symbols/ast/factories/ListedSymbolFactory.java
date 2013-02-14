package cs444.parser.symbols.ast.factories;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.ListedSymbol;


public class ListedSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from){
        if(!ANonTerminal.class.isInstance(from)) return from;

        ANonTerminal nonTerm = (ANonTerminal) from;
        if(nonTerm.children.size() == 2){
            if(nonTerm.children.get(0).getName().equals(nonTerm.getName())) return new ListedSymbol(nonTerm, 0);
            else if(nonTerm.children.get(1).getName().equals(nonTerm.getName())) return new ListedSymbol(nonTerm, 1);
        }

        return from;
    }
}
