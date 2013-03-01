package cs444.types;

import cs444.CompilerException;
import cs444.ast.EmptyVisitor;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.types.exceptions.DuplicateDeclarationException;

public class LocalDclLinker extends EmptyVisitor {
    private LocalScope currentScope;
    private final String enclosingClassName; // used for exception message

    public LocalDclLinker(String enclosingClassName){
        this.enclosingClassName = enclosingClassName;
    }

    @Override
    public void close(DclSymbol dclSymbol) throws CompilerException {
        // in close because we cannot used this variable inside its initializer
        String varName = dclSymbol.dclName;
        if (currentScope.isDeclared(varName)) throw new DuplicateDeclarationException(varName, enclosingClassName);
        currentScope.add(varName, dclSymbol);
    }

    @Override
    public void open(JoosNonTerminal aNonTerminal){
        if (aNonTerminal.getName().equals(JoosNonTerminal.BLOCK)){
            currentScope = new LocalScope(currentScope);
        }
    }

    @Override
    public void close(JoosNonTerminal aNonTerminal){
        if (aNonTerminal.getName().equals(JoosNonTerminal.BLOCK)){
            currentScope = currentScope.parent;
        }
    }

    @Override
    public void visit(NameSymbol nameSymbol){
        // TODO: store a reference to the DclSymbol node that this name refers to
    }
}
