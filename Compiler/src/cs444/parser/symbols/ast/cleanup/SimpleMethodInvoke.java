package cs444.parser.symbols.ast.cleanup;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.ICodeGenVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;

public class SimpleMethodInvoke extends ANonTerminal implements Typeable {
    public final MethodOrConstructorSymbol call;

    public SimpleMethodInvoke(final MethodInvokeSymbol invoke) {
        super("Method Invoke");
        this.call = invoke.getCallSymbol();
        children.addAll(invoke.getArgs());
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(final ICodeGenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeSymbol getType() {
        return call.getType();
    }

    @Override
    public void setType(final TypeSymbol type) {
        call.setType(type);
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }
}