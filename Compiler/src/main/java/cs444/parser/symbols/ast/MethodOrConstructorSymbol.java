package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.Platform;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.static_analysis.ReachabilityAnalyzer;
import cs444.types.APkgClassResolver;
import cs444.types.LocalDclLinker;

import java.util.Collection;
import java.util.List;

public abstract class MethodOrConstructorSymbol extends AModifiersOptSymbol {

    public final List<DclSymbol> params;
    public APkgClassResolver resolver;
    boolean alreadyLinked = false;
    private boolean reachabilityAnalized = false;

    public MethodOrConstructorSymbol(String nodeName, MethodHeader header,
                                     ANonTerminal from, ANonTerminal body, TypeSymbol type)
            throws IllegalModifierException, UnsupportedException {
        super(nodeName, header.name.value, from, type);

        params = header.params;
        if (body != null) {
            children.addAll(body.children);
        }
    }

    @Override
    public ProtectionLevel defaultProtectionLevel() {
        return ProtectionLevel.NOT_VALID;
    }

    @Override
    public ImplementationLevel defaultImplementationLevel() {
        return ImplementationLevel.NORMAL;
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);

        for (ISymbol param : params) {
            param.accept(visitor);
        }

        visitor.middle(this);

        for (ISymbol child : children) {
            child.accept(visitor);
        }

        visitor.close(this);
    }

    public void resolveLocalVars(String enclosingClassName, Collection<Platform<?, ?>> platforms) throws CompilerException {
        if (alreadyLinked) {
            return;
        }
        accept(new LocalDclLinker(enclosingClassName, platforms));
        alreadyLinked = true;
    }

    public void analyzeReachability(String enclosingClassName) throws CompilerException {
        if (reachabilityAnalized) {
            return;
        }

        accept(new ReachabilityAnalyzer(enclosingClassName));

        reachabilityAnalized = true;
    }

    public boolean isVoid() {
        return type.value == JoosNonTerminal.VOID;
    }

    public boolean isAbstract() {
        return getImplementationLevel() == ImplementationLevel.ABSTRACT;
    }
}