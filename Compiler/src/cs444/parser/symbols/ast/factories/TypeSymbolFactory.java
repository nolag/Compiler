package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class TypeSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        if(!from.getName().equals(JoosNonTerminal.TYPE)
           && !from.getName().equalsIgnoreCase(JoosNonTerminal.VOID)
           && !from.getName().equals(JoosNonTerminal.ARRAY_TYPE)) return from;

        if (from.getName().equals(JoosNonTerminal.ARRAY_TYPE)){
            // This cover this case: o instanceof int[];
            ISymbol stype = ((JoosNonTerminal)from).getChildren().get(0);
            String typeValue;
            if (stype instanceof Terminal){
                Terminal type = (Terminal) stype;
                typeValue = type.value;
            }else{
                NameSymbol type = (NameSymbol) stype;
                typeValue = type.value;
            }
            return new TypeSymbol(typeValue, true, true);
        }

        if (from.getName().equalsIgnoreCase(JoosNonTerminal.VOID)){
            return new TypeSymbol(JoosNonTerminal.VOID, false, false);
        }

        ANonTerminal nonTerm = (ANonTerminal) from;
        ISymbol typeChild = nonTerm.children.get(0);

        boolean isArray = false;

        if(typeChild.getName().equals(JoosNonTerminal.ARRAY_TYPE)){
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
