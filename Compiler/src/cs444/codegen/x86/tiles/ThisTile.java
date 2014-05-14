package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.Thisable;

public class ThisTile implements ITile<X86Instruction, Size, Thisable>{
    private static ThisTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile =  new ThisTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).thisables.add(tile);
    }

    private ThisTile() { }

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
