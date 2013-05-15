package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;

public class Extern implements X86Instruction {
    private final String lbl;

    public Extern(String lbl){
        this.lbl = "extern " + lbl;
    }

    public Extern(InstructionArg what){
        this(what.getValue());
    }

    @Override
    public String generate() {
        return lbl;
    }

}
