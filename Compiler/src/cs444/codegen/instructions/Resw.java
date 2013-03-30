package cs444.codegen.instructions;

public class Resw extends ReserveInstruction implements Instruction {

    public Resw(String name, long quantity) {
        super(name, quantity);
    }

    @Override
    protected String getResName() {
        return "resw";
    }

}
