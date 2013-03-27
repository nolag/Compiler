package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public class Cmp implements Instruction{
    private final InstructionArg arg1;
    private final InstructionArg arg2;

    public Cmp(InstructionArg arg1, InstructionArg arg2){
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public String generate() {
        return "cmp " + arg1.getValue() + ", " + arg2.getValue();
    }

}
