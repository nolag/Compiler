package cs444.codegen.instructions;

public class Ret implements Instruction {
    public static final Ret RET = new Ret();

    private Ret(){ }

    @Override
    public String generate() {
        return "ret";
    }

}
