package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;

import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.expressions.NotOpExprSymbol;

public class NotTile implements ITile<X86Instruction, Size, NotOpExprSymbol>{
    private static NotTile tile;

    public static NotTile getTile() {
        if(tile == null) tile = new NotTile();
        return tile;
    }

    private NotTile() { }

    @Override
    public boolean fits(final NotOpExprSymbol op, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final NotOpExprSymbol notSymbol,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.addAll(platform.getBest(notSymbol.children.get(0)));
        instructions.add(new Xor(Register.ACCUMULATOR, Immediate.TRUE, sizeHelper));
        return instructions;
    }

}
