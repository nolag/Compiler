package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class TypeSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        if(!from.getName().equalsIgnoreCase("Type")
           && !from.getName().equalsIgnoreCase("void")) return from;

        if (from.getName().equalsIgnoreCase("void")){
            return new TypeSymbol("void", false, false);
        }

        ANonTerminal nonTerm = (ANonTerminal) from;
        ISymbol typeChild = nonTerm.children.get(0);

        boolean isArray = false;

        if(typeChild.getName().equalsIgnoreCase("ArrayType")){
            isArray = true;
            //This drops the brackets, if they are needed for multi dim array then it will need to change
            nonTerm = (ANonTerminal) typeChild;
            typeChild = nonTerm.children.get(0);
        }

        ATerminal primitive = (ATerminal)typeChild;
        String name = primitive.value;

        return new TypeSymbol(name, isArray, false);
    }

}
