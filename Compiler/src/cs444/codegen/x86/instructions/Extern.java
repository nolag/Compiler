package cs444.codegen.x86.instructions;

import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Extern extends X86Instruction {
    private final String lbl;

    public Extern(final String lbl){
        super(0);
        this.lbl = "extern " + lbl;
    }

    public Extern(final Immediate what){
        this(what.toString());
    }

    @Override
    public String generate() {
        return lbl;
    }

    @Override
    public final boolean uses(final InstructionArg what) {
        return false;
    }
}
