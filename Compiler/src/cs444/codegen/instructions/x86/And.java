package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;

public class And implements X86Instruction {
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
