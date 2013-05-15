package cs444.codegen.instructions.x86;


public class Resd extends ReserveInstruction implements X86Instruction {

    public Resd(String name, long quantity) {
        super(name, quantity);
    }

    @Override
    protected String getResName() {
        return "resd";
    }

}
