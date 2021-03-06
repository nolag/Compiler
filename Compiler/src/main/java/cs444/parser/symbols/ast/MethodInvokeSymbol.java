package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.types.LookupLink;

import java.util.List;

public class MethodInvokeSymbol extends ANonTerminal implements Typeable {
    public final boolean hasFirst;
    public final String methodName;
    public final String lookupFirst;

    private LookupLink link;
    private MethodOrConstructorSymbol call;

    public MethodInvokeSymbol(String name, List<ISymbol> params, ISymbol on) {
        super("Method Invoke");
        if (on != null) {
            children.add(on);
        }
        String[] parts = name.split("\\.");

        if (parts.length != 1) {
            name = name.substring(0, name.lastIndexOf("."));
            lookupFirst = name;
        } else {
            lookupFirst = null;
        }

        children.addAll(params);
        hasFirst = on != null;

        methodName = parts[parts.length - 1];
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.prepare(this);
        int i = 0;
        if (hasFirst) {
            children.get(0).accept(visitor);
            i++;
        }
        visitor.open(this);
        for (; i < children.size(); i++) {
            ISymbol param = children.get(i);
            param.accept(visitor);
        }
        visitor.close(this);
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    public LookupLink getLookup() {
        return link;
    }

    public void setLookup(LookupLink link) {
        this.link = link;
    }

    public List<ISymbol> getArgs() {
        if (hasFirst) {
            return children.subList(1, children.size());
        }
        return children;
    }

    public MethodOrConstructorSymbol getCallSymbol() {
        return call;
    }

    public void setCallSymbol(MethodOrConstructorSymbol call) {
        this.call = call;
    }

    @Override
    public TypeSymbol getType() {
        return link.getType();
    }

    @Override
    public void setType(TypeSymbol type) { }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }
}
