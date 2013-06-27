package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.Xor;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.CmpMaker;
import cs444.codegen.instructions.x86.factories.UniOpMaker;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class CompOpTile<T extends BinOpExpr> extends BinOpTile<T>{
    private final UniOpMaker uni;
    protected CompOpTile(final UniOpMaker uni) {
        super(CmpMaker.maker);
        this.uni = uni;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final T bin, final Platform<X86Instruction> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = super.generate(bin, platform);
        final X86SizeHelper sizeHelper = (X86SizeHelper) platform.getSizeHelper();
        instructions.add(new Comment("Xor here CAN change the setl bit?"));
        instructions.add(uni.make(Register.DATA, sizeHelper));
        instructions.add(new Comment("clear all bits in register"));
        instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, Register.DATA, Size.LOW, sizeHelper));
        return instructions;
    }
}
