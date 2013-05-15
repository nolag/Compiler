package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public class Sete implements X86Instruction{
    private final InstructionArg arg;

    public Sete(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "sete " + arg.getValue(Size.LOW);
    }

}
