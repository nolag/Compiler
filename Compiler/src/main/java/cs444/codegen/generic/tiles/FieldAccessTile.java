package cs444.codegen.generic.tiles;

import cs444.codegen.IRuntime;
import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.FieldAccessSymbol;

@SuppressWarnings("rawtypes")
public class FieldAccessTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, FieldAccessSymbol> {

    private static FieldAccessTile tile;

    private FieldAccessTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> FieldAccessTile<T, E> getTile() {
        if (tile == null) {
            tile = new FieldAccessTile();
        }
        return tile;
    }

    @Override
    public boolean fits(FieldAccessSymbol symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(FieldAccessSymbol field, Platform<T, E> platform) {

        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        instructions.addAll(platform.getBest(field.children.get(0)));
        platform.getTileHelper().setupJmpNull(IRuntime.EXCEPTION_LBL, platform.getSizeHelper(), instructions);
        instructions.addAll(platform.getBest(field.children.get(1)));
        return instructions;
    }
}