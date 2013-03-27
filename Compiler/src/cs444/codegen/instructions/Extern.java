package cs444.codegen.instructions;

public class Extern implements Instruction {
    private final String lbl;

    public Extern(String lbl){
        this.lbl = "extern " + lbl;
    }

    @Override
    public String generate() {
        return lbl;
    }

}
