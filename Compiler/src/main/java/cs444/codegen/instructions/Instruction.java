package cs444.codegen.instructions;


public abstract class Instruction <T extends Instruction<T>> {
    public final int time;
    public final int space;
    
    protected Instruction(final int time, final int space){
        this.time = time;
        this.space = space;
    }
    
    public abstract boolean uses(InstructionArg<T, ?> what);
    
    public abstract String generate();
}
