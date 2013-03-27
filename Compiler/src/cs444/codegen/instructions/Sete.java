package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;

public class Sete implements Instruction{
    private final InstructionArg arg;

    public Sete(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "sete " + arg.getValue(Size.LOW);
    }

}
