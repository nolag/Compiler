package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;

public class Jmp implements X86Instruction{
    private final InstructionArg arg1;

    public Jmp(InstructionArg arg1){
        this.arg1 = arg1;
    }

    @Override
    public String generate() {
        return "jmp " + arg1.getValue();
    }

}
