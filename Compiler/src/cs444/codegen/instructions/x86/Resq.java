package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;


public class Resq extends ReserveInstruction implements X86Instruction {

    public Resq(final String name, final long quantity) {
        super(name, quantity);
    }

    @Override
    protected String getResName() {
        return "resq";
    }

}
