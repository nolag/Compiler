package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.parser.symbols.ast.Thisable;

public class ThisTile implements ITile<X86Instruction, X86SizeHelper, Thisable>{
    public static void init(){
        new ThisTile();
    }

    private ThisTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).thisables.add(this);
    }

    @Override
    public boolean fits(final Thisable op) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final Thisable symbol,
            final Platform<X86Instruction, X86SizeHelper> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        instructions.add(new Comment("This (or super) pointer"));
        instructions.add(new Mov(Register.ACCUMULATOR, Memory.getThisPointer(sizeHelper), sizeHelper));
        return instructions;
    }

}