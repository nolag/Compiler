package cs444.codegen.instructions;


public abstract class Instruction{
    public final long cost;

    protected Instruction(final long cost){
        this.cost = cost;
    }

    public abstract String generate();
}
