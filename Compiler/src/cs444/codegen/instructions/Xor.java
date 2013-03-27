package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public class Xor implements Instruction {
    public final InstructionArg arg1;
    public final InstructionArg arg2;

    public Xor(InstructionArg arg1, InstructionArg arg2){
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public String generate() {
        return "xor " + arg1.getValue() + ", " + arg2.getValue();
    }
}
