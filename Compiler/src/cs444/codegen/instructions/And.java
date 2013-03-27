package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public class And implements Instruction {
    public final InstructionArg arg1;
    public final InstructionArg arg2;

    public And(InstructionArg addTo, InstructionArg addWith){
        this.arg1 = addTo;
        this.arg2 = addWith;
    }

    @Override
    public String generate() {
        return "and " + arg1.getValue() + ", " + arg2.getValue();
    }
}
