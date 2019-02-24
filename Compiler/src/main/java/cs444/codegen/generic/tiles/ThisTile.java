package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.Thisable;

@SuppressWarnings("rawtypes")
public class ThisTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, Thisable> {

    private static ThisTile tile;

    private ThisTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> ThisTile<T, E> getTile() {
        if (tile == null) {
            tile = new ThisTile();
        }
        return tile;
    }

    @Override
    public boolean fits(Thisable op, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(Thisable symbol, Platform<T, E> platform) {
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        TileHelper<T, E> tileHelper = platform.getTileHelper();
        tileHelper.loadThisToDefault(instructions, platform.getSizeHelper());
        return instructions;
    }
}
