package cs444.parser.symbols.ast.factories;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.ClassSymbol;
import cs444.parser.symbols.ast.FieldSymbol;
import cs444.parser.symbols.ast.IdSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class FieldSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ANonTerminal from) throws UnsupportedException, IllegalModifierException {
        if(!AInterfaceOrClassSymbol.class.isInstance(from)) return from;
        List<ISymbol> fields = new LinkedList<ISymbol>();
        List<ANonTerminal> parents = new LinkedList<ANonTerminal>();;

        for(ISymbol child : from.children){
            List<ISymbol> cfields = ((ANonTerminal)child).getAll("FieldDecl");
            if(cfields.size() != 0){
                fields.addAll(cfields);
                parents.add((ANonTerminal)child);
            }
        }

        if(fields.size() != 0 && !ClassSymbol.class.isInstance(from)) throw new UnsupportedException(from.getClass() + " with fields");

        for(int i = 0; i < fields.size(); i++){
            ISymbol symbol = fields.get(i);
            //I don't think it matters where the field is as long as they are in the same order
            ANonTerminal child = (ANonTerminal)symbol;
            List<ISymbol> ids = child.getAll("Id");
            String type = ((IdSymbol)ids.get(0)).fullName;
            String name = ((Terminal)ids.get(1)).token.lexeme;
            from.children.add(new FieldSymbol(name, parents.get(i), type));
        }

        for(ISymbol parent : parents)from.children.remove(parent);

        return from;
    }
}
