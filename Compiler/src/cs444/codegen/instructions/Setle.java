package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;

public class Setle implements Instruction{
    private final InstructionArg arg;

    public Setle(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "setle " + arg.getValue(Size.LOW);
    }

}
