package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public class Jg implements Instruction{
    private final InstructionArg arg;

    public Jg(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "jg " + arg.getValue();
    }

}
