package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;

import cs444.parser.symbols.ast.Thisable;

@SuppressWarnings("rawtypes")
public class ThisTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, Thisable> {
    

    
@SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> ThisTile<T, E> getTile() {
        if (tile == null) tile = new ThisTile();
        return tile;
    }

    private ThisTile() {}

    @Override
    public boolean fits(final Thisable op, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final Thisable symbol, final Platform<T, E> platform) {
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final TileHelper<T, E> tileHelper = platform.getTileHelper();
        tileHelper.loadThisToDefault(instructions, platform.getSizeHelper());
        return instructions;
    }

}