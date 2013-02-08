package cs444.parser.symbols.ast.factories;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.ast.AInterfaceOrClass;
import cs444.parser.symbols.ast.ClassSymbol;
import cs444.parser.symbols.ast.InterfaceSymbol;
import cs444.parser.symbols.ast.ListedSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class ClassInterfaceFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ANonTerminal from) throws OutOfRangeException, UnsupportedException {
        if(from.getName() != "ClassOrInterfaceDeclaration") return from;

        List<Terminal> modifiers = new LinkedList<Terminal>();

        ANonTerminal modiferChild = (ListedSymbol)from.firstOrDefault("ModifiersOpt");

        if(modiferChild != null){
            for(ISymbol child : modiferChild.getChildren()) modifiers.add((Terminal)child);
        }

        ANonTerminal declaration = (ANonTerminal) from.firstOrDefault("ClassDeclaration");

        String bodyName;
        boolean isInterface;

        if(declaration != null){
            bodyName = "ClassBody";
            isInterface = false;
        }else{
            declaration = (ANonTerminal)from.firstOrDefault("InterfaceDeclaration");
            bodyName = "InterfaceBody";
            isInterface = true;
        }

        AInterfaceOrClass classInterface = null;
        String classOrInterfaceName = ((Terminal)declaration.firstOrDefault("Identifier")).token.lexeme;
        ANonTerminal interfaces =(ANonTerminal) declaration.firstOrDefault("TypeList");
        ANonTerminal body =(ANonTerminal) declaration.firstOrDefault(bodyName);

        List<String> interfaceNames = new LinkedList<String>();

        try{
            for(ISymbol child : interfaces.children){
                TypeSymbol typeSymbol = (TypeSymbol) child;
                if(typeSymbol.isArray) throw new UnsupportedException("extend an array");
                interfaceNames.add(typeSymbol.type);
            }
        }catch (ClassCastException e){
            throw new UnsupportedException("extend or implement basic types");
        }

        if(isInterface){
            classInterface = new InterfaceSymbol(classOrInterfaceName, interfaceNames, body);
        }else{
            TypeSymbol superType = (TypeSymbol) declaration.firstOrDefault("Type");
            if(superType.isArray) throw new UnsupportedException("extend an array");
            if(declaration.firstOrDefault("BasicType") != null) throw new UnsupportedException("extend basic types");
            String superName = superType == null ? null : superType.type;
            classInterface = new ClassSymbol(classOrInterfaceName, superName, interfaceNames, body);
        }

        classInterface.validate();
        return classInterface;
    }
}
