package cs444.codegen.x86.instructions.bases;

import cs444.codegen.instructions.InstructionArg;



public abstract class ReserveInstruction extends X86Instruction {
    protected final String name;
    protected final long quantity;

    protected abstract String getResName();

    public ReserveInstruction(final String name, final long quantity, final int time, final int size) {
        super(time, size);
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String generate() {
        return name + ": \t" + this.getResName() + "\t " + quantity;
    }

    @Override
    public final boolean uses(final InstructionArg<X86Instruction, ?> what) {
        return false;
    }
}
