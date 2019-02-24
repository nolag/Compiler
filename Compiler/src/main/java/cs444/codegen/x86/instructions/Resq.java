package cs444.codegen.x86.instructions;

import cs444.codegen.x86.instructions.bases.ReserveInstruction;

public class Resq extends ReserveInstruction {

    public Resq(String name, long quantity) {
        super(name, quantity, 0, 0);
    }

    @Override
    protected String getResName() {
        return "resq";
    }
}
