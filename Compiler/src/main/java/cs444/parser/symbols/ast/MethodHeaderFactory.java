package cs444.parser.symbols.ast;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

import java.util.LinkedList;
import java.util.List;

public class MethodHeaderFactory {
    public static MethodHeader buildForRegularMethod(ANonTerminal methodHeader) throws IllegalModifierException,
            UnsupportedException {
        NameSymbol name;
        TypeSymbol type;
        List<DclSymbol> dcls;

        type = (TypeSymbol) methodHeader.firstOrDefault("Type");

        ANonTerminal methodDecl = (ANonTerminal) methodHeader.firstOrDefault("MethodDeclarator");
        name = (NameSymbol) methodDecl.firstOrDefault("Name");

        ANonTerminal params = (ANonTerminal) methodDecl.firstOrDefault("FormalParameterList");

        dcls = buildParamsList(params);

        return new MethodHeader(name, type, dcls);
    }

    public static MethodHeader buildForConstructor(ANonTerminal constructorDeclarator) throws IllegalModifierException, UnsupportedException {
        NameSymbol name = ((NameSymbol) constructorDeclarator.firstOrDefault("Name"));

        ANonTerminal params = (ANonTerminal) constructorDeclarator.firstOrDefault("FormalParameterList");
        List<DclSymbol> dcls = buildParamsList(params);

        return new MethodHeader(name, null, dcls);
    }

    private static List<DclSymbol> buildParamsList(ANonTerminal params)
            throws IllegalModifierException, UnsupportedException {
        List<DclSymbol> dclList = new LinkedList<DclSymbol>();
        if (params != null) {
            for (ISymbol symbol : params.children) {
                ANonTerminal param = (ANonTerminal) symbol;
                TypeSymbol ptype = (TypeSymbol) param.firstOrDefault("Type");
                NameSymbol pname = (NameSymbol) param.firstOrDefault("Name");
                dclList.add(new DclSymbol(pname.value, null, ptype, null, true));
            }
        }
        return dclList;
    }
}
