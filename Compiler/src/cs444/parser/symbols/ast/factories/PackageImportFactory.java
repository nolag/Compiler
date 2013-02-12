package cs444.parser.symbols.ast.factories;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.QualifiedIdSymbol;
import cs444.parser.symbols.ast.QualifiedIdSymbol.Type;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class PackageImportFactory extends ASTSymbolFactory{

    private void convertNext(ANonTerminal from, List<Integer> remove, int on, Type type){
        QualifiedIdSymbol qid = (QualifiedIdSymbol)from.children.get(on + 1);
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
            }else if(from.children.get(i).getName().equalsIgnoreCase("IMPORT")){
                convertNext(from, remove, i, Type.IMPORT);
            }
        }

        for(int delete : remove) from.children.remove(delete);

        return from;
    }
}
