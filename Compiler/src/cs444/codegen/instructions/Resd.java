package cs444.codegen.instructions;

public class Resd extends ReserveInstruction implements Instruction {

    public Resd(String name, long quantity) {
        super(name, quantity);
    }

    @Override
    protected String getResName() {
        return "resd";
    }

}
