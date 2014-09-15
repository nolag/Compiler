package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.NullSymbol;

public class NullTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, NullSymbol> {
    private static NullTile<?, ?> tile;

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> void init(final Class<? extends Platform<T, E>> klass) {
        if (tile == null) tile = new NullTile<T, E>();
        TileSet.<T, E> getOrMake(klass).nulls.add((ITile<T, E, NullSymbol>) tile);
    }

    private NullTile() {}

    @Override
    public boolean fits(final NullSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final NullSymbol symbol, final Platform<T, E> platform) {
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        platform.getTileHelper().loadNumberToDefault(TileHelper.NULL, instructions, sizeHelper);
        return instructions;
    }
}
