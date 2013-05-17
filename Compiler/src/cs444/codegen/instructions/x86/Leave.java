package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;


public class Leave implements X86Instruction{
    public static final Leave LEAVE = new Leave();

    private Leave(){ }

    @Override
    public String generate() {
        return "leave";
    }
}
