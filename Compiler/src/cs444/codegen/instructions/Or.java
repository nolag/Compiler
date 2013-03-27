package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public class Or implements Instruction {
    public final InstructionArg arg1;
    public final InstructionArg arg2;

    public Or(InstructionArg addTo, InstructionArg addWith){
        this.arg1 = addTo;
        this.arg2 = addWith;
    }

    @Override
    public String generate() {
        return "or " + arg1.getValue() + ", " + arg2.getValue();
    }
}
