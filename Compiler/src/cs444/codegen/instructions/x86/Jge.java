package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;

public class Jge implements X86Instruction{
    private final InstructionArg arg;

    public Jge(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "jge " + arg.getValue();
    }

}
