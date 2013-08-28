package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;


public class BoolTile  <T extends Instruction, E extends Enum<E>> implements ITile<T, E, BooleanLiteralSymbol>{
    public static <T extends Instruction, E extends Enum<E>> void init(final Class<T> klass){
        new BoolTile<T, E>(klass);
    }

    private BoolTile(final Class<T> klass){
        TileSet.<T, E>getOrMake(klass).bools.add(this);
    }

    @Override
    public boolean fits(final BooleanLiteralSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final BooleanLiteralSymbol boolSymbol,
            final Platform<T, E> platform) {

        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        final TileHelper<T, E> tileHelper = platform.getTileHelper();

        tileHelper.loadBool(boolSymbol.boolValue, instructions, sizeHelper);

        return instructions;
    }
}
