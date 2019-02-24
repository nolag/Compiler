package cs444.codegen.generic.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

@SuppressWarnings("rawtypes")
public class ZeroMultTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, MultiplyExprSymbol> {
    private static ZeroMultTile tile;

    private ZeroMultTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> ZeroMultTile<T, E> getTile() {
        if (tile == null) {
            tile = new ZeroMultTile();
        }
        return tile;
    }

    @Override
    public boolean fits(MultiplyExprSymbol symbol, Platform<T, E> platform) {
        ISymbol first = symbol.children.get(0);
        ISymbol second = symbol.children.get(1);
        return TileHelper.isZero(first) || TileHelper.isZero(second);
    }

    @Override
    public InstructionsAndTiming<T> generate(MultiplyExprSymbol op, Platform<T, E> platform) {
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        if (TileHelper.isZero(op.children.get(0))) {
            instructions.addAll(platform.getBest(op.children.get(1)));
        } else {
            instructions.addAll(platform.getBest(op.children.get(0)));
        }
        platform.zeroDefaultLocation(instructions);
        return instructions;
    }
}
