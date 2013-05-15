package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public class Setne implements X86Instruction{
    private final InstructionArg arg;

    public Setne(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "setne " + arg.getValue(Size.LOW);
    }

}
