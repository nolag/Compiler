package cs444.parser.symbols.ast.factories;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.ClassSymbol;
import cs444.parser.symbols.ast.InterfaceSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class ClassInterfaceFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws UnsupportedException, IllegalModifierException {
        if(!from.getName().equalsIgnoreCase("TypeDeclaration")) return from;

        ANonTerminal nonTerm = (ANonTerminal) from;
        ANonTerminal declaration = (ANonTerminal) nonTerm.firstOrDefault("ClassDeclaration");

        String bodyName;
        boolean isInterface;

        if(declaration != null){
            bodyName = "ClassBody";
            isInterface = false;
        }else{
            declaration = (ANonTerminal)nonTerm.firstOrDefault("InterfaceDeclaration");
            bodyName = "InterfaceBody";
            isInterface = true;
        }

        AInterfaceOrClassSymbol classInterface = null;

        String classOrInterfaceName = ((ATerminal)declaration.firstOrDefault("Name")).lexeme;
        ANonTerminal interfaces =(ANonTerminal) declaration.firstOrDefault("InterfaceTypeList");
        ANonTerminal body =(ANonTerminal) declaration.firstOrDefault(bodyName);

        List<String> interfaceNames = new LinkedList<String>();

        if(interfaces != null){
            try{
                for(ISymbol child : interfaces.children){
                    NameSymbol nameSymbol = (NameSymbol) child;
                    interfaceNames.add(nameSymbol.lexeme);
                }
            }catch (ClassCastException e){
                throw new UnsupportedException("extend or implement basic types");
            }
        }

        List<ISymbol> children = Collections.emptyList();
        if(body != null) children = body.children;

        if(isInterface){
            classInterface = new InterfaceSymbol(classOrInterfaceName, declaration, interfaceNames, children);
        }else{
            ANonTerminal superName = (ANonTerminal)declaration.firstOrDefault("ClassName");
            String superType = null;
            if(superName != null){
                superType = ((NameSymbol) superName.firstOrDefault("Name")).lexeme;
            }
            classInterface = new ClassSymbol(classOrInterfaceName, declaration, interfaceNames, children, superType);
        }

        return classInterface;
    }
}
