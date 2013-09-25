package cs444.codegen.x86.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public abstract class UniInstruction extends X86Instruction{
    private final String what;
    public final InstructionArg data;
    public final Size size;
    public final SizeHelper<X86Instruction, Size> sizeHelper;

    protected UniInstruction(final String what, final InstructionArg data, final Size size,
            final SizeHelper<X86Instruction, Size> sizeHelper, final long cost){

        super(cost);
        this.what = what;
        this.data = data;
        this.size = size;
        this.sizeHelper = sizeHelper;
    }

    protected UniInstruction(final String what, final InstructionArg data, final SizeHelper<X86Instruction, Size> sizeHelper, final long cost) {
        this(what, data, sizeHelper.getDefaultSize(), sizeHelper, cost);
    }

    @Override
    public final String generate() {
        return what + " " + data.getValue(size, sizeHelper);
    }

    @Override
    public boolean uses(final InstructionArg what) {
        return data.uses(what);
    }
}