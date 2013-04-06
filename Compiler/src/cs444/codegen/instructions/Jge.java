package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public class Jge implements Instruction{
    private final InstructionArg arg;

    public Jge(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "jge " + arg.getValue();
    }

}
