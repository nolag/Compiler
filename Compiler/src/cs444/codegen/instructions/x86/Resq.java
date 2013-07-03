package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.ReserveInstruction;


public class Resq extends ReserveInstruction {

    public Resq(final String name, final long quantity) {
        super(name, quantity, 0);
    }

    @Override
    protected String getResName() {
        return "resq";
    }

}
