package cs444.codegen;

public class PointerRegister extends InstructionArg{
    public final Register register;
    public final int offset;

    public PointerRegister(Register register, int offset){
        this.register = register;
        this.offset = offset;
    }

    public PointerRegister(Register register){
        this.register = register;
        this.offset = 0;
    }

    @Override
    public String getValue(Size size) {
        if(offset == 0) return "[" + register.get32() + "]";
        return "[" + register.get32() + " + " + offset + "]";
    }
}
