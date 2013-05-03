package cs444.codegen;

public class PointerRegister extends InstructionArg{
    public static final PointerRegister THIS = new PointerRegister(Register.FRAME, SizeHelper.DEFAULT_STACK_SIZE * 2);
    public static final PointerRegister ZEROING_REGISTER_4 = new PointerRegister(Register.ACCUMULATOR, Immediate.C_TIMES_4);
    public static final PointerRegister ZEROING_REGISTER_2 = new PointerRegister(Register.ACCUMULATOR, Immediate.C_TIMES_2);
    public static final PointerRegister ZEROING_REGISTER_1 = new PointerRegister(Register.ACCUMULATOR, Register.COUNTER);

    public final InstructionArg arg;
    public final InstructionArg offsetArg;
    public final long offset;

    private PointerRegister(InstructionArg arg, InstructionArg offsetArg, long offset){
        this.arg = arg;
        this.offsetArg = offsetArg;
        this.offset = offset;
    }

    public PointerRegister(InstructionArg arg, long offset){
        this(arg, null, offset);
    }

    public PointerRegister(InstructionArg arg, InstructionArg offset){
        this(arg, offset, 0);
    }

    public PointerRegister(InstructionArg arg){
        this(arg, null, 0);
    }

    @Override
    public String getValue(Size size) {
        if(offset == 0 && offsetArg == null) return "[" + arg.getValue() + "]";
        if(null != offsetArg) return "[" + arg.getValue() + " + " + offsetArg.getValue() + "]";
        return "[" + arg.getValue() + " + " + offset + "]";
    }
}
