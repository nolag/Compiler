package cs444.parser.symbols.ast.factories;

import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
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
        List<ISymbol> fields = from.getAll("FieldDecl");

        if(fields.size() != 0 && !ClassSymbol.class.isInstance(from)) throw new UnsupportedException(from.getClass() + " with fields");

        for(ISymbol symbol : fields){
            //I don't think it matters where the field is as long as they are in the same order
            from.children.remove(symbol);
            ANonTerminal child = (ANonTerminal)symbol;
            IdSymbol id = (IdSymbol)child.firstOrDefault("Id");
            String name = id.fullName;
            from.children.add(new FieldSymbol(name, child));
        }

        return from;
    }
}
