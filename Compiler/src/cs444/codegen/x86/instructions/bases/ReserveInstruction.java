package cs444.codegen.x86.instructions.bases;

import cs444.codegen.x86.InstructionArg;



public abstract class ReserveInstruction extends X86Instruction {
    protected final String name;
    protected final long quantity;

    protected abstract String getResName();

    public ReserveInstruction(final String name, final long quantity, final long cost) {
        super(cost);
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String generate() {
        return name + ": \t" + this.getResName() + "\t " + quantity;
    }

    @Override
    public final boolean writesTo(final InstructionArg what) {
        return false;
    }
}
