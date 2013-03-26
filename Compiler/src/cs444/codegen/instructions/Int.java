package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public class Int implements Instruction {
    private final InstructionArg arg;

    public Int(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "int " + arg.getValue();
    }

}
