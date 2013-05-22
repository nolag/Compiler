package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.ReserveInstruction;


public class Resd extends ReserveInstruction{

    public Resd(final String name, final long quantity) {
        super(name, quantity, 0);
    }

    @Override
    protected String getResName() {
        return "resd";
    }
}
