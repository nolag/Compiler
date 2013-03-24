package cs444.codegen.instructions;

public class Global implements Instruction{
    private final String lbl;

    public Global(String lbl){
        this.lbl = "global " + lbl;
    }

    @Override
    public String generate() {
        return lbl;
    }

}
