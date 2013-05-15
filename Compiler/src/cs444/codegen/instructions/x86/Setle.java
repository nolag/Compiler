package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public class Setle implements X86Instruction{
    private final InstructionArg arg;

    public Setle(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "setle " + arg.getValue(Size.LOW);
    }

}
