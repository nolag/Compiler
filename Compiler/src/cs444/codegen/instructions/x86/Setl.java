package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public class Setl implements X86Instruction{
    private final InstructionArg arg;

    public Setl(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "setl " + arg.getValue(Size.LOW);
    }

}
