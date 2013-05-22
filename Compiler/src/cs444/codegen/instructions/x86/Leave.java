package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;


public class Leave extends X86Instruction{
    public static final Leave LEAVE = new Leave();

    private Leave(){
        super(5);
    }

    @Override
    public String generate() {
        return "leave";
    }
}
