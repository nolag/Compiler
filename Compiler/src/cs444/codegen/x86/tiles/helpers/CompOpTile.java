package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.CmpMaker;
import cs444.codegen.x86.instructions.factories.UniOpMaker;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class CompOpTile<T extends BinOpExpr> extends BinOpTile<T>{
    private final UniOpMaker uni;
    protected CompOpTile(final UniOpMaker uni) {
        super(CmpMaker.maker);
        this.uni = uni;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final T bin, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = super.generate(bin, platform);
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.add(new Comment("Xor here CAN change the setl bit?"));
        instructions.add(uni.make(Register.DATA, sizeHelper));
        instructions.add(new Comment("clear all bits in register"));
        instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, Register.DATA, Size.LOW, sizeHelper));
        return instructions;
    }

    @Override
    public final boolean fits(final T op, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        boolean isOk;
        final Typeable ts1 = (Typeable) op.children.get(0);
        final Typeable ts2 = (Typeable) op.children.get(1);
        isOk = sizeHelper.getDefaultStackSize()  >= sizeHelper.getByteSizeOfType(ts1.getType().getTypeDclNode().fullName);
        isOk |= sizeHelper.getDefaultStackSize()  >= sizeHelper.getByteSizeOfType(ts2.getType().getTypeDclNode().fullName);
        return isOk;
    }
}
