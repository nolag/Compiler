package cs444.codegen.instructions.x86;


public abstract class ReserveInstruction implements X86Instruction {
    protected final String name;
    protected final long quantity;

    protected abstract String getResName();

    public ReserveInstruction(String name, long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String generate() {
        return name + ": \t" + this.getResName() + "\t " + quantity;
    }
}
