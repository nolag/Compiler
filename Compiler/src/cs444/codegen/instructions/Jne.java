package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public class Jne implements Instruction{
    private final InstructionArg arg1;

    public Jne(InstructionArg arg1){
        this.arg1 = arg1;
    }

    @Override
    public String generate() {
        return "jne " + arg1.getValue();
    }

}
