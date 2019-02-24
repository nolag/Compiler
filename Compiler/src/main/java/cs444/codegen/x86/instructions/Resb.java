package cs444.codegen.x86.instructions;

import cs444.codegen.x86.instructions.bases.ReserveInstruction;

public class Resb extends ReserveInstruction {

    public Resb(String name, long quantity) {
        super(name, quantity, 0, 0);
    }

    @Override
    protected String getResName() {
        return "resb";
    }
}
