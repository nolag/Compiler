package cs444.codegen.generic.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;

@SuppressWarnings("rawtypes")
public class RemZeroTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, RemainderExprSymbol> {

    private static RemZeroTile tile;

    private RemZeroTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> RemZeroTile<T, E> getTile() {
        if (tile == null) {
            tile = new RemZeroTile();
        }
        return tile;
    }

    @Override
    public boolean fits(RemainderExprSymbol symbol, Platform<T, E> platform) {
        return TileHelper.isZero(symbol.children.get(1));
    }

    @Override
    public InstructionsAndTiming<T> generate(RemainderExprSymbol op, Platform<T, E> platform) {
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        instructions.addAll(platform.getBest(op.children.get(0)));
        platform.getRunime().throwException(instructions, JoosNonTerminal.DIV_ZERO);
        return instructions;
    }
}
