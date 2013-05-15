package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;

public class Xor implements X86Instruction {
    public final InstructionArg arg1;
    public final InstructionArg arg2;

    public Xor(InstructionArg arg1, InstructionArg arg2){
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public String generate() {
        return "xor " + arg1.getValue() + ", " + arg2.getValue();
    }
}
