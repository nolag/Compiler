package cs444.codegen.x86.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Size;

public abstract class ShXd extends X86Instruction {
    public final String what;
    public final InstructionArg<X86Instruction, Size> arg1;
    public final InstructionArg<X86Instruction, Size> arg2;
    public final InstructionArg<X86Instruction, Size> arg3;
    public final SizeHelper<X86Instruction, Size> sizeHelper;

    protected ShXd(String what, InstructionArg<X86Instruction, Size> arg1,
                   InstructionArg<X86Instruction, Size> arg2, InstructionArg<X86Instruction, Size> arg3,
                   SizeHelper<X86Instruction, Size> sizeHelper, int time, int size) {
        super(time, size);
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public final String generate() {
        Size size = sizeHelper.getDefaultSize();
        return what + " " + arg1.getValue(size, sizeHelper) + ", " + arg2.getValue(size, sizeHelper)
                + ", " + arg3.getValue(Size.LOW, sizeHelper);
    }

    @Override
    public final boolean uses(InstructionArg<X86Instruction, ?> what) {
        return arg1.uses(what);
    }
}
