package cs444.codegen;

public class PointerRegister extends InstructionArg{
    public static final PointerRegister THIS = new PointerRegister(Register.FRAME, SizeHelper.DEFAULT_STACK_SIZE * 2);

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
