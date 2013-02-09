package cs444.parser.symbols.ast.factories;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ast.ListedSymbol;


public class ListedSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ANonTerminal convert(ANonTerminal from){
        if(from.children.size() == 2){
            if(from.children.get(0).getName().equals(from.getName())) from = new ListedSymbol(from, 0);
            else if(from.children.get(1).getName().equals(from.getName())) from = new ListedSymbol(from, 1);
        }
        return from;
    }
}
