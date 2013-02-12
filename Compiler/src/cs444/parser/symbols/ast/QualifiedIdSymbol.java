package cs444.parser.symbols.ast;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;

public class QualifiedIdSymbol extends ANonTerminal{
    public static enum Type { ID_SYMBOL, PACKAGE, IMPORT };

    public final String fullName;
    public Type type = Type.ID_SYMBOL;

    public QualifiedIdSymbol(Terminal idOne, ListedSymbol rest) {
        super("QualifiedId");
        children.add(idOne);

        StringBuilder sb = new StringBuilder(idOne.token.lexeme);

        if(rest != null){
            for(ISymbol symbol : rest.children){
                Terminal part = (Terminal) symbol;
                children.add(symbol);
                sb.append("." + part.token.lexeme);
            }
        }

        fullName = sb.toString();
    }

}
