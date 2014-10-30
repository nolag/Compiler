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
public class LongCastTile<T extends Instruction<T>, E extends Enum<E>> extends LongOnlyTile<T, E, CastExpressionSymbol> {

    private static LongCastTile tile;

@SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> LongCastTile<T, E> getTile() {
        if (tile == null) tile = new LongCastTile();
        return tile;
    }

    private LongCastTile() {}

    @Override
    public boolean fits(final CastExpressionSymbol symbol, final Platform<T, E> platform) {
        return super.fits(symbol, platform) && !X86TileHelper.isReferenceType(symbol);
    }

    @Override
    public InstructionsAndTiming<T> generate(final CastExpressionSymbol symbol, final Platform<T, E> platform) {

        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();

        final Typeable from = (Typeable) symbol.getOperandExpression();

        instructions.addAll(platform.getBest(from));
        platform.getTileHelper().makeLong(from, instructions, sizeHelper);

        return instructions;
    }
}
