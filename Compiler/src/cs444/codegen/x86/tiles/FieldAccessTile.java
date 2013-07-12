package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.X86Platform;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86_32.linux.Runtime;
import cs444.parser.symbols.ast.FieldAccessSymbol;

public class FieldAccessTile implements ITile<X86Instruction, Size, FieldAccessSymbol>{

    public static void init(){
        new FieldAccessTile();
    }

    private FieldAccessTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).fieldAccess.add(this);
    }

    @Override
    public boolean fits(final FieldAccessSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final FieldAccessSymbol field,
            final Platform<X86Instruction, Size> platform) {

        final X86Platform x86Platform = (X86Platform)platform;
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        instructions.addAll(platform.getBest(field.children.get(0)));
        platform.getTileHelper().ifNullJmpCode(Runtime.EXCEPTION_LBL, x86Platform.getSizeHelper(), instructions);
        instructions.addAll(platform.getBest(field.children.get(1)));
        return instructions;
    }
}