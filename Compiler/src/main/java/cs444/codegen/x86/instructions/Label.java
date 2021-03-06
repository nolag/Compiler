package cs444.codegen.x86.instructions;

import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Label extends X86Instruction {
    private final String lbl;

    public Label(String lbl) {
        super(0, 0);
        this.lbl = lbl + ":";
    }

    @Override
    public String generate() {
        return lbl;
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        return false;
    }
}
