package cs444.codegen;

public class PointerRegister extends InstructionArg{
    public final InstructionArg arg;
    public final long offset;

    public PointerRegister(InstructionArg arg, long offset){
        this.arg = arg;
        this.offset = offset;
    }

    public PointerRegister(InstructionArg arg){
        this.arg = arg;
        this.offset = 0;
    }

    @Override
    public String getValue(Size size) {
        if(offset == 0) return "[" + arg.getValue(Size.DWORD) + "]";
        return "[" + arg.getValue(Size.DWORD) + " + " + offset + "]";
    }
}
