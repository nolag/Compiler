package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.NullSymbol;

@SuppressWarnings("rawtypes")
public class NullTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, NullSymbol> {

    private static NullTile tile;

    private NullTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> NullTile<T, E> getTile() {
        if (tile == null) {
            tile = new NullTile();
        }
        return tile;
    }

    @Override
    public boolean fits(NullSymbol symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(NullSymbol symbol, Platform<T, E> platform) {
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        platform.getTileHelper().loadNumberToDefault(TileHelper.NULL, instructions, sizeHelper);
        return instructions;
    }
}
