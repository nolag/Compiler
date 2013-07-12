package cs444.codegen.x86.instructions;

import cs444.codegen.x86.instructions.bases.ReserveInstruction;


public class Resb extends ReserveInstruction {

    public Resb(final String name, final long quantity) {
        super(name, quantity, 0);
    }

    @Override
    protected String getResName() {
        return "resb";
    }

}
