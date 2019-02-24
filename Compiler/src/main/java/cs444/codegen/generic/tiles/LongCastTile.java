package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

@SuppressWarnings("rawtypes")
public class LongCastTile<T extends Instruction<T>, E extends Enum<E>> extends LongOnlyTile<T, E,
        CastExpressionSymbol> {

    private static LongCastTile tile;

    private LongCastTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> LongCastTile<T, E> getTile() {
        if (tile == null) {
            tile = new LongCastTile();
        }
        return tile;
    }

    @Override
    public boolean fits(CastExpressionSymbol symbol, Platform<T, E> platform) {
        return super.fits(symbol, platform) && !X86TileHelper.isReferenceType(symbol);
    }

    @Override
    public InstructionsAndTiming<T> generate(CastExpressionSymbol symbol, Platform<T, E> platform) {

        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();

        Typeable from = (Typeable) symbol.getOperandExpression();

        instructions.addAll(platform.getBest(from));
        platform.getTileHelper().makeLong(from, instructions, sizeHelper);

        return instructions;
    }
}
