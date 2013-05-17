package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;


public class Resb extends ReserveInstruction implements X86Instruction {

    public Resb(String name, long quantity) {
        super(name, quantity);
    }

    @Override
    protected String getResName() {
        return "resb";
    }

}
