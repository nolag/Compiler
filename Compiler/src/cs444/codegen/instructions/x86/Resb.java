package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.ReserveInstruction;


public class Resb extends ReserveInstruction {

    public Resb(final String name, final long quantity) {
        super(name, quantity, 0);
    }

    @Override
    protected String getResName() {
        return "resb";
    }

}
