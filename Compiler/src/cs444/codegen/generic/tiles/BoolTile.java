package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;

import cs444.parser.symbols.ast.BooleanLiteralSymbol;

@SuppressWarnings("rawtypes")
public class BoolTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, BooleanLiteralSymbol> {
        private static BoolTile tile;

    
@SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> BoolTile<T, E> getTile() {
        if (tile == null) tile = new BoolTile();
        return tile;
    }

    private BoolTile() {}

    @Override
    public boolean fits(final BooleanLiteralSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final BooleanLiteralSymbol boolSymbol, final Platform<T, E> platform) {

        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        final TileHelper<T, E> tileHelper = platform.getTileHelper();

        tileHelper.loadBool(boolSymbol.boolValue, instructions, sizeHelper);

        return instructions;
    }
}
