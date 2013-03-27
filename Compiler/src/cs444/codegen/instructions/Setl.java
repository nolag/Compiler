package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;

public class Setl implements Instruction{
    private final InstructionArg arg;

    public Setl(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "setl " + arg.getValue(Size.LOW);
    }

}
