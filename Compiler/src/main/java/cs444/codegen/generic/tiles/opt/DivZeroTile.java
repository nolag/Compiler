package cs444.codegen.generic.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;

@SuppressWarnings("rawtypes")
public class DivZeroTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, DivideExprSymbol> {
    private static DivZeroTile tile;

    private DivZeroTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> DivZeroTile<T, E> getTile() {
        if (tile == null) {
            tile = new DivZeroTile();
        }
        return tile;
    }

    @Override
    public boolean fits(DivideExprSymbol symbol, Platform<T, E> platform) {
        return TileHelper.isZero(symbol.children.get(1));
    }

    @Override
    public InstructionsAndTiming<T> generate(DivideExprSymbol op, Platform<T, E> platform) {
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        instructions.addAll(platform.getBest(op.children.get(0)));
        platform.getRunime().throwException(instructions, JoosNonTerminal.DIV_ZERO);
        return instructions;
    }
}
