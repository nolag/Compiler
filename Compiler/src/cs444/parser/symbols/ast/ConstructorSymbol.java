package cs444.parser.symbols.ast;

import cs444.codegen.ICodeGenVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class ConstructorSymbol extends MethodOrConstructorSymbol{
    public ConstructorSymbol(MethodHeader header, ANonTerminal from, ANonTerminal body)
            throws IllegalModifierException, UnsupportedException {

        super("ConstructorDeclaration", header, from, body, null);
    }



    @Override
    public void validate() throws UnsupportedException {
        if(getImplementationLevel() != ImplementationLevel.NORMAL) throw new UnsupportedException("Unimlemented and final constructors");
        if(isNative())throw new UnsupportedException("native constructors");

        super.validate();
    }



    @Override
    public void accept(ICodeGenVisitor visitor) {
        visitor.visit(this);
    }
}
