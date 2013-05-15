package cs444.codegen.instructions.x86;


public class Ret implements X86Instruction {
    public static final Ret RET = new Ret();

    private Ret(){ }

    @Override
    public String generate() {
        return "ret";
    }

}
