package cs444.parser.symbols.ast;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class MethodHeader {
    public final NameSymbol name;
    public final TypeSymbol type;
    public final Iterable<DclSymbol> dcls;

    public MethodHeader(ANonTerminal methodHeader) throws IllegalModifierException, UnsupportedException {
        List<DclSymbol> dclList = new LinkedList<DclSymbol>();
        type = (TypeSymbol) methodHeader.firstOrDefault("Type");

        ANonTerminal methodDecl = (ANonTerminal)methodHeader.firstOrDefault("MethodDeclarator");
        name = (NameSymbol) methodDecl.firstOrDefault("Name");

        ANonTerminal params = (ANonTerminal)methodDecl.firstOrDefault("FormalParameterList");

        if(params != null){
            for(ISymbol symbol : params.children){
                ANonTerminal param = (ANonTerminal) symbol;
                TypeSymbol ptype = (TypeSymbol) param.firstOrDefault("Type");
                NameSymbol pname = (NameSymbol) param.firstOrDefault("Name");
                dclList.add(new DclSymbol(pname.value, null, ptype, null, true));
            }
        }
        dcls = dclList;
    }
}
