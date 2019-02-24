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

    private BoolTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> BoolTile<T, E> getTile() {
        if (tile == null) {
            tile = new BoolTile();
        }
        return tile;
    }

    @Override
    public boolean fits(BooleanLiteralSymbol symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(BooleanLiteralSymbol boolSymbol, Platform<T, E> platform) {

        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        TileHelper<T, E> tileHelper = platform.getTileHelper();

        tileHelper.loadBool(boolSymbol.boolValue, instructions, sizeHelper);

        return instructions;
    }
}
