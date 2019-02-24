package cs444.codegen.x86.instructions.bases;

import cs444.codegen.instructions.InstructionArg;

public abstract class ReserveInstruction extends X86Instruction {
    protected final String name;
    protected final long quantity;

    public ReserveInstruction(String name, long quantity, int time, int size) {
        super(time, size);
        this.name = name;
        this.quantity = quantity;
    }

    protected abstract String getResName();

    @Override
    public String generate() {
        return name + ": \t" + getResName() + "\t " + quantity;
    }

    @Override
    public final boolean uses(InstructionArg<X86Instruction, ?> what) {
        return false;
    }
}
