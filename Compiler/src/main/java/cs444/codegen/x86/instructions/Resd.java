package cs444.codegen.x86.instructions;

import cs444.codegen.x86.instructions.bases.ReserveInstruction;

public class Resd extends ReserveInstruction {

    public Resd(String name, long quantity) {
        super(name, quantity, 0, 0);
    }

    @Override
    protected String getResName() {
        return "resd";
    }
}
