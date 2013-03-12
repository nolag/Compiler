package cs444.parser.symbols.ast.factories;

import java.util.Collections;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class MethodInvokeSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        if(!from.getName().equalsIgnoreCase(JoosNonTerminal.METHOD_INVOCATION)) return from;
        ANonTerminal nonTerm = (ANonTerminal) from;
        NameSymbol name = (NameSymbol) nonTerm.lastOrDefault("Name");

        ANonTerminal argList = (ANonTerminal) nonTerm.firstOrDefault(JoosNonTerminal.ARGUMENT_LIST);

        Iterable<ISymbol> args = Collections.emptyList();
        if(argList != null) args = argList.children;

        ISymbol before = nonTerm.children.get(0);
        if(before == name) before = null;

        return new MethodInvokeSymbol(name.value, args, before);
    }
}
