package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;

public abstract class DataInstruction implements X86Instruction{

    protected final InstructionArg data;

    public DataInstruction(InstructionArg data) {
        this.data = data;
    }
}