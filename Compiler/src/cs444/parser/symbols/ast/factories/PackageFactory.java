package cs444.parser.symbols.ast.factories;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.IdSymbol;
import cs444.parser.symbols.ast.IdSymbol.Type;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class PackageFactory extends ASTSymbolFactory{

    private void convertNext(ANonTerminal from, List<Integer> remove, int on, Type type){
        IdSymbol qid = (IdSymbol)from.children.get(on + 1);
        qid.type = type;
        remove.add(on);
    }

    @Override
    protected ISymbol convert(ANonTerminal from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        //This is unneeded to work but should be more efficient.
        if(!from.name.toUpperCase().equals("COMPILATIONUNIT")) return from;

        List<Integer> remove = new LinkedList<Integer>();

        for(int i = 0; i < from.children.size() - 1; i++){
            if(from.children.get(i).getName().equalsIgnoreCase("PACKAGE")){
                convertNext(from, remove, i, Type.PACKAGE);
            }
        }

        for(int delete : remove) from.children.remove(delete);

        return from;
    }
}
