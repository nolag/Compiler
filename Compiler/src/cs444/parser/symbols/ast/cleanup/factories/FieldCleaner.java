package cs444.parser.symbols.ast.cleanup.factories;

import cs444.CompilerException;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.ThisSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;

public class FieldCleaner extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(final ISymbol from) throws CompilerException {
        if(from instanceof FieldAccessSymbol){
            final FieldAccessSymbol fas = (FieldAccessSymbol) from;
            final ISymbol first = fas.children.get(0);
            final ISymbol second = fas.children.get(1);

            if(first instanceof SimpleNameSymbol){
                if(((SimpleNameSymbol)first).getType().isClass) return convert(second);
            }

            if(second instanceof FieldAccessSymbol){
                final FieldAccessSymbol fas2 = (FieldAccessSymbol) second;
                final ISymbol secondFirst = fas2.children.get(0);
                if(secondFirst instanceof ThisSymbol){
                    return new FieldAccessSymbol(fas.children.get(0), fas2.children.get(1));
                }
            }
        }

        return from;
    }

}
