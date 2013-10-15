package cs444.codegen.x86.instructions;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Label extends X86Instruction{
    private final String lbl;

    public Label(final String lbl){
        super(0, 0);
        this.lbl = lbl + ":";
    }

    @Override
    public String generate() {
        return lbl;
    }

    @Override
    public boolean uses(final InstructionArg what) {
        return false;
    }
}
