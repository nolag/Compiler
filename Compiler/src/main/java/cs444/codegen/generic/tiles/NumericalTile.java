package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.INumericLiteral;

@SuppressWarnings("rawtypes")
public class NumericalTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, INumericLiteral> {

    private static NumericalTile tile;

    private NumericalTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> NumericalTile<T, E> getTile() {
        if (tile == null) {
            tile = new NumericalTile();
        }
        return tile;
    }

    @Override
    public boolean fits(INumericLiteral symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(INumericLiteral num, Platform<T, E> platform) {

        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<>();
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        TileHelper<T, E> tileHelper = platform.getTileHelper();
        tileHelper.loadNumberToDefault(num, instructions, sizeHelper);
        return instructions;
    }
}
