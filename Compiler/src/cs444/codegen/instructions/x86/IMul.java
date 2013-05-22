package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class IMul extends X86Instruction{
    private final InstructionArg arg1;
    private final InstructionArg arg2;
    private final X86SizeHelper sizeHelper;

    public IMul(final InstructionArg arg1, final X86SizeHelper sizeHelper){
        //13-42 and 12-42 is close enough
        super(27);
        this.arg1 = arg1;
        this.arg2 = null;
        this.sizeHelper = sizeHelper;
    }

    public IMul(final Register arg1, final InstructionArg arg2, final X86SizeHelper sizeHelper){
        //13-42 rounds up to 28
        super(28);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public String generate() {
        if(arg2 == null) return "imul " + arg1.getValue(sizeHelper);
        return "imul " + arg1.getValue(sizeHelper) + ", " + arg2.getValue(sizeHelper);
    }
}
