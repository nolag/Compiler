package cs444.codegen.instructions.x86;


public class Resw extends ReserveInstruction implements X86Instruction {

    public Resw(String name, long quantity) {
        super(name, quantity);
    }

    @Override
    protected String getResName() {
        return "resw";
    }

}
