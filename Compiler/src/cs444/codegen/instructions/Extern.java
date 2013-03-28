package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public class Extern implements Instruction {
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
