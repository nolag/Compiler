package cs444.codegen.x86.instructions;

import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Extern extends X86Instruction {
    private final String lbl;

    public Extern(String lbl) {
        super(0, 0);
        this.lbl = "extern " + lbl;
    }

    public Extern(Immediate what) {
        this(what.toString());
    }

    @Override
    public String generate() {
        return lbl;
    }

    @Override
    public final boolean uses(InstructionArg<X86Instruction, ?> what) {
        return false;
    }
}
