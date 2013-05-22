package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;


public class Label extends X86Instruction{
    private final String lbl;

    public Label(final String lbl){
        super(0);
        this.lbl = lbl + ":";
    }

    @Override
    public String generate() {
        return lbl;
    }
}
