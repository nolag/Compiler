package cs444.codegen.x86.instructions;

import cs444.codegen.x86.instructions.bases.ReserveInstruction;


public class Resd extends ReserveInstruction{

    public Resd(final String name, final long quantity) {
        super(name, quantity, 0, 0);
    }

    @Override
    protected String getResName() {
        return "resd";
    }
}
