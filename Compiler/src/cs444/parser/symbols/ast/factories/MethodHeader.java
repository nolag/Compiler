package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;

class MethodHeader {
    ANonTerminal methodHeader;
    public MethodHeader(ANonTerminal methodHeader) {
        this.methodHeader = methodHeader;
    }

    public TypeSymbol getType() {
        return (TypeSymbol) methodHeader.firstOrDefault("Type");
    }

    public NameSymbol getName() {
        ANonTerminal methodDecl = (ANonTerminal)methodHeader.firstOrDefault("MethodDeclarator");
        return (NameSymbol) methodDecl.firstOrDefault("Name");
    }
}
