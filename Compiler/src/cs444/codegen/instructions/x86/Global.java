package cs444.codegen.instructions.x86;


public class Global implements X86Instruction{
    private final String lbl;

    public Global(String lbl){
        this.lbl = "global " + lbl;
    }

    @Override
    public String generate() {
        return lbl;
    }

}
