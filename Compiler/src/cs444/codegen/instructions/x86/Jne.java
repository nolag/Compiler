package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;

public class Jne implements X86Instruction{
    private final InstructionArg arg1;

    public Jne(InstructionArg arg1){
        this.arg1 = arg1;
    }

    @Override
    public String generate() {
        return "jne " + arg1.getValue();
    }

}
