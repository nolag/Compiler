package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;

public class Setne implements Instruction{
    private final InstructionArg arg;

    public Setne(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "setne " + arg.getValue(Size.LOW);
    }

}
