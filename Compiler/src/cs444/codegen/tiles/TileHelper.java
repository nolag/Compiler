package cs444.codegen.tiles;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.INumericLiteral;

public class TileHelper {
    public static boolean isZero(final ISymbol symbol){
        if(symbol instanceof INumericLiteral){
            final INumericLiteral numLit = (INumericLiteral) symbol;
            return numLit.getValue() == 0;
        }
        return false;
    }
}
