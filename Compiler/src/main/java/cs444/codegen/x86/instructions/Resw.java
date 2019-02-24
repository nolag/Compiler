package cs444.codegen.x86.instructions;

import cs444.codegen.x86.instructions.bases.ReserveInstruction;

public class Resw extends ReserveInstruction {

    public Resw(String name, long quantity) {
        super(name, quantity, 0, 0);
    }

    @Override
    protected String getResName() {
        return "resw";
    }
}
