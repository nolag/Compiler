package cs444.codegen;

public class OffsetRegister extends InstructionArg {
    private final Register reg;
    private final int offset;

    public OffsetRegister(Register reg, int offset){
        this.reg = reg;
        this.offset = offset;
    }

    @Override
    public String getValue(Size size) {
        return reg.get32() + " + " + offset;
    }

}
