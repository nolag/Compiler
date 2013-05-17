package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.Immediate;

public class Extern implements X86Instruction {
    private final String lbl;

    public Extern(final String lbl){
        this.lbl = "extern " + lbl;
    }

    public Extern(final Immediate what){
        this(what.getValue());
    }

    @Override
    public String generate() {
        return lbl;
    }

}
