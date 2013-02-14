package cs444.parser.symbols.ast.factories;

import cs444.lexer.Token;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.NameSymbol.Type;

public class NameSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) {

        if(from.getName().equals(Token.Type.ID.toString())){
            ATerminal terminal = (ATerminal) from;
            return new NameSymbol(terminal.lexeme, Type.ID_SYMBOL);
        }

        if(!ANonTerminal.class.isInstance(from)) return from;

        ANonTerminal nonTerm = (ANonTerminal) from;

        Type type = Type.ID_SYMBOL;

        if(nonTerm.name.equalsIgnoreCase("PackageDeclaration")){
            nonTerm = (ANonTerminal) nonTerm.firstOrDefault("Name");
            type = Type.PACKAGE;
        }else if(nonTerm.name.equalsIgnoreCase("ImportDeclaration")){
            type = null == nonTerm.firstOrDefault("Star") ? Type.IMPORT : Type.STAR_IMPORT;
            nonTerm = (ANonTerminal) nonTerm.firstOrDefault("Name");
        }

        if(nonTerm.name.equalsIgnoreCase("SimpleName")){
            ATerminal id = (ATerminal)nonTerm.firstOrDefault("Id");
            return new NameSymbol(id.lexeme, type);
        }

        if(!nonTerm.name.equalsIgnoreCase("Name")) return nonTerm;

        StringBuilder sb = new StringBuilder();
        for(ISymbol symbol : nonTerm.getAll("Id")){
            ATerminal child = (ATerminal) symbol;
            sb.append(child.lexeme).append(".");
        }

        return new NameSymbol(sb.substring(0, sb.length() - 1), type);
    }

}
