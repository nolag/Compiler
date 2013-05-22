package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.ReserveInstruction;


public class Resw extends ReserveInstruction {

    public Resw(final String name, final long quantity) {
        super(name, quantity, 0);
    }

    @Override
    protected String getResName() {
        return "resw";
    }

}
