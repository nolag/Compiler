package cs444.codegen.instructions;

public class Resb extends ReserveInstruction implements Instruction {

    public Resb(String name, long quantity) {
        super(name, quantity);
    }

    @Override
    protected String getResName() {
        return "resb";
    }

}
