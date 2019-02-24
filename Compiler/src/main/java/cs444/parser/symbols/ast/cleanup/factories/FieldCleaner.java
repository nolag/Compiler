package cs444.parser.symbols.ast.cleanup.factories;

import cs444.CompilerException;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.ThisSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;

public class FieldCleaner extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(ISymbol from) throws CompilerException {
        if (from instanceof FieldAccessSymbol) {
            FieldAccessSymbol fas = (FieldAccessSymbol) from;
            ISymbol first = fas.children.get(0);
            ISymbol second = fas.children.get(1);

            if (first instanceof SimpleNameSymbol) {
                if (((SimpleNameSymbol) first).getType().isClass) {
                    return convert(second);
                }
            }

            if (second instanceof FieldAccessSymbol) {
                FieldAccessSymbol fas2 = (FieldAccessSymbol) second;
                ISymbol secondFirst = fas2.children.get(0);
                if (secondFirst instanceof ThisSymbol) {
                    Typeable typeable = (Typeable) fas2.children.get(1);
                    return new FieldAccessSymbol(fas.children.get(0), typeable, typeable.getType());
                }
            }
        }

        return from;
    }
}
