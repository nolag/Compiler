package cs444.codegen.instructions.x86;


public class Label implements X86Instruction{
    private final String lbl;

    public Label(String lbl){
        this.lbl = lbl + ":";
    }

    @Override
    public String generate() {
        return lbl;
    }
}
