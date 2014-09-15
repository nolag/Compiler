package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.ReturnExprSymbol;

public class RetTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, ReturnExprSymbol> {
    private static RetTile<?, ?> tile;

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> void init(final Class<? extends Platform<T, E>> klass) {
        if (tile == null) tile = new RetTile<T, E>();
        TileSet.<T, E> getOrMake(klass).rets.add((ITile<T, E, ReturnExprSymbol>) tile);
    }

    private RetTile() {}

    @Override
    public boolean fits(final ReturnExprSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final ReturnExprSymbol retSymbol, final Platform<T, E> platform) {

        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        if (retSymbol.children.size() == 1) instructions.addAll(platform.getBest(retSymbol.children.get(0)));
        platform.getTileHelper().methEpilogue(instructions);
        return instructions;
    }

}
