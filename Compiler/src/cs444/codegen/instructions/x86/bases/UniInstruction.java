package cs444.codegen.instructions.x86.bases;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.X86SizeHelper;

public abstract class UniInstruction extends X86Instruction{
    public final String what;
    public final InstructionArg data;
    public final Size size;
    public final X86SizeHelper sizeHelper;

    protected UniInstruction(final String what, final InstructionArg data, final Size size, final X86SizeHelper sizeHelper, final long cost){
        super(cost);
        this.what = what;
        this.data = data;
        this.size = size;
        this.sizeHelper = sizeHelper;
    }

    protected UniInstruction(final String what, final InstructionArg data, final X86SizeHelper sizeHelper, final long cost) {
        this(what, data, sizeHelper.defaultStack, sizeHelper, cost);
    }

    @Override
    public final String generate() {
        return what + " " + data.getValue(size, sizeHelper);
    }
}