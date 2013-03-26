package cs444.codegen.instructions;

public class Leave implements Instruction{
    public static final Leave LEAVE = new Leave();

    private Leave(){ }

    @Override
    public String generate() {
        return "leave";
    }
}
