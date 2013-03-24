package cs444.codegen.instructions;

public class Label implements Instruction{
    private final String lbl;

    public Label(String lbl){
        this.lbl = lbl + ":";
    }

    @Override
    public String generate() {
        return lbl;
    }
}
