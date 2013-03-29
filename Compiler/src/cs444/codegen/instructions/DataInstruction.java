package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public abstract class DataInstruction implements Instruction{

    protected final InstructionArg data;

    public DataInstruction(InstructionArg data) {
        this.data = data;
    }
}