package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.helpers.LongOnlyTile;
import cs444.parser.symbols.ast.INumericLiteral;

public class LongNumberTile extends LongOnlyTile<INumericLiteral> {
    private static LongNumberTile tile;

    public static void init() {
        if(tile == null) tile = new LongNumberTile();
        TileSet.<X86Instruction, Size>getOrMake(X86_32Platform.class).numbs.add(tile);
    }

    private LongNumberTile() { }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final INumericLiteral num, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.add(new Comment("putting long " + num.getAsLongValue() + " in eax and edx"));
        final int aval = (int) num.getAsLongValue();
        final int dval = (int) (num.getAsLongValue() >> 32);
        final Immediate aimd = new Immediate(aval);
        final Immediate dimd = new Immediate(dval);
        instructions.add(new Mov(Register.ACCUMULATOR, aimd, sizeHelper));
        instructions.add(new Mov(Register.DATA, dimd, sizeHelper));
        return instructions;
    }
}
