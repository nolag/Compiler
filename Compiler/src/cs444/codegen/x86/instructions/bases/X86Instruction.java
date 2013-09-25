package cs444.codegen.x86.instructions.bases;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.x86.InstructionArg;

public abstract class X86Instruction extends Instruction{
    protected X86Instruction(final long cost){
        super(cost);
    }

    public abstract boolean uses(InstructionArg what);
}
