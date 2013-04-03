package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public class Je implements Instruction{
    private final InstructionArg arg;

    public Je(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "je " + arg.getValue();
    }

}
