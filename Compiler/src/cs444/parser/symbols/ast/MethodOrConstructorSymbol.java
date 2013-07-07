package cs444.parser.symbols.ast;

import java.util.List;

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

public abstract class MethodOrConstructorSymbol extends AModifiersOptSymbol {

    public APkgClassResolver resolver;

    public final List<DclSymbol> params;

    public MethodOrConstructorSymbol(final String nodeName, final MethodHeader header,
            final ANonTerminal from, final ANonTerminal body, final TypeSymbol type)
            throws IllegalModifierException, UnsupportedException {
        super(nodeName, header.name.value, from, type);

        this.params = header.params;
        if(body != null) children.addAll(body.children);
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
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);

        for (final ISymbol param : this.params) {
            param.accept(visitor);
        }

        visitor.middle(this);

        for (final ISymbol child : children) {
            child.accept(visitor);
        }

        visitor.close(this);
    }

    private boolean arelocalVarsLinked = false;
    public void resolveLocalVars(final String enclosingClassName, final Platform<?, ?> platform) throws CompilerException {
        if (arelocalVarsLinked) return;

        this.accept(new LocalDclLinker(enclosingClassName, platform));

        arelocalVarsLinked = true;
    }

    private boolean reachabilityAnalized = false;
    public void analyzeReachability(final String enclosingClassName) throws CompilerException {
        if (reachabilityAnalized) return;

        this.accept(new ReachabilityAnalyzer(enclosingClassName));

        reachabilityAnalized = true;
    }

    public boolean isVoid() {
        return type.value == JoosNonTerminal.VOID;
    }

    public boolean isAbstract() {
        return this.getImplementationLevel() == ImplementationLevel.ABSTRACT;
    }
}