package cs444.parser.symbols.ast.cleanup.factories;

import cs444.CompilerException;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;

public class FieldFlattener extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(ISymbol from) throws CompilerException {
        if (from instanceof FieldAccessSymbol) {
            FieldAccessSymbol fa1 = (FieldAccessSymbol) from;
            if (fa1.children.get(0) instanceof FieldAccessSymbol) {
                FieldAccessSymbol fa2 = (FieldAccessSymbol) convert(fa1.children.get(0));
                ISymbol is1 = fa2.children.get(0);
                ISymbol is2 = convert(fa2.children.get(1));
                Typeable is3 = (Typeable) convert(fa1.children.get(1));
                TypeSymbol type = is3.getType();
                ISymbol newFa2 = convert(new FieldAccessSymbol(is2, is3, type));
                return convert(new FieldAccessSymbol(is1, newFa2, type));
            }
        }
        return from;
    }
}
