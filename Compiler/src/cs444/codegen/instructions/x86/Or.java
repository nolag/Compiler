package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public class Or implements X86Instruction {
    public final InstructionArg arg1;
    public final InstructionArg arg2;
    public final X86SizeHelper sizeHelper;

    public Or(final InstructionArg addTo, final InstructionArg addWith, final X86SizeHelper sizeHelper){
        this.arg1 = addTo;
        this.arg2 = addWith;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public String generate() {
        return "or " + arg1.getValue(sizeHelper) + ", " + arg2.getValue(sizeHelper);
    }
}
