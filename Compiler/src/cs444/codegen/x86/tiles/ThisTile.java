package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.parser.symbols.ast.Thisable;

public class ThisTile implements ITile<X86Instruction, Size, Thisable>{
    public static void init(){
        new ThisTile();
    }

    private ThisTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).thisables.add(this);
    }

    @Override
    public boolean fits(final Thisable op, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final Thisable symbol, final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.add(new Comment("This (or super) pointer"));
        instructions.add(new Mov(Register.ACCUMULATOR, Memory.getThisPointer(sizeHelper), sizeHelper));
        return instructions;
    }

}
