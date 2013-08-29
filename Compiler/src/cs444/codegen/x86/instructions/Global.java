package cs444.codegen.x86.instructions;

import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Global extends X86Instruction{
    private final String lbl;

    public Global(final String lbl){
        super(0);
        this.lbl = "global " + lbl;
    }

    @Override
    public String generate() {
        return lbl;
    }
}