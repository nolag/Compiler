package cs444.types;

import cs444.CompilerException;
import cs444.ast.EmptyVisitor;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.types.exceptions.DuplicateDeclarationException;

public class LocalDclLinker extends EmptyVisitor {
    private LocalScope currentScope;
    private final String enclosingClassName; // used for exception message

    public LocalDclLinker(String enclosingClassName){
        this.enclosingClassName = enclosingClassName;
    }

    // creates root scope which will contain parameters declarations
    @Override
    public void open(MethodOrConstructorSymbol methodSymbol){
        pushNewScope();
    }

    @Override
    public void close(MethodOrConstructorSymbol methodSymbol){
        popCurrentScope();
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
            pushNewScope();
        }
    }

    @Override
    public void close(JoosNonTerminal aNonTerminal){
        if (aNonTerminal.getName().equals(JoosNonTerminal.BLOCK)){
            popCurrentScope();
        }
    }

    private void pushNewScope() {
        currentScope = new LocalScope(currentScope);
    }

    private void popCurrentScope() {
        currentScope = currentScope.parent;
    }

    @Override
    public void visit(NameSymbol nameSymbol){
        // TODO: store a reference to the DclSymbol node that this name refers to. Required for A3
    }
}
