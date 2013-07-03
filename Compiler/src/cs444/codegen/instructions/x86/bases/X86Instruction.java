package cs444.codegen.instructions.x86.bases;

import cs444.codegen.instructions.Instruction;

public abstract class X86Instruction extends Instruction{
    protected X86Instruction(final long cost){
        super(cost);
    }
}
