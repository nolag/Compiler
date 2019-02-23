package cs444.parser.symbols.ast.cleanup.factories;

import cs444.CompilerException;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;

public class FieldFlattener extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(final ISymbol from) throws CompilerException {
        if(from instanceof FieldAccessSymbol){
            final FieldAccessSymbol fa1 = (FieldAccessSymbol) from;
            if(fa1.children.get(0) instanceof FieldAccessSymbol){
                final FieldAccessSymbol fa2 = (FieldAccessSymbol)convert(fa1.children.get(0));
                final ISymbol is1 = fa2.children.get(0);
                final ISymbol is2 = convert(fa2.children.get(1));
                final Typeable is3 = (Typeable)convert(fa1.children.get(1));
                final TypeSymbol type = is3.getType();
                final ISymbol newFa2 = convert(new FieldAccessSymbol(is2, is3, type));
                return convert(new FieldAccessSymbol(is1, newFa2, type));
            }
        }
        return from;
    }
}
