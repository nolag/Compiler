package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.x86_32.linux.Runtime;
import cs444.parser.symbols.ast.FieldAccessSymbol;

public class FieldAccessTile<T extends Instruction, E extends Enum<E>> implements ITile<T, E, FieldAccessSymbol>{

    public static <T extends Instruction, E extends Enum<E>> void init(final Class<? extends Platform<T, E>> klass){
        new FieldAccessTile<T, E>(klass);
    }

    private FieldAccessTile(final Class<? extends Platform<T, E>> klass){
        TileSet.<T, E>getOrMake(klass).fieldAccess.add(this);
    }

    @Override
    public boolean fits(final FieldAccessSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final FieldAccessSymbol field,
            final Platform<T, E> platform) {

        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        instructions.addAll(platform.getBest(field.children.get(0)));
        platform.getTileHelper().ifNullJmpCode(Runtime.EXCEPTION_LBL, platform.getSizeHelper(), instructions);
        instructions.addAll(platform.getBest(field.children.get(1)));
        return instructions;
    }
}