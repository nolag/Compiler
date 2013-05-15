package cs444.codegen.x86;



public class PointerRegister extends InstructionArg{
    public static final PointerRegister THIS = new PointerRegister(Register.FRAME, SizeHelper.DEFAULT_STACK_SIZE * 2);
    public static final PointerRegister ZEROING_REGISTER_8 = new PointerRegister(Register.ACCUMULATOR, Immediate.C_TIMES_8);
    public static final PointerRegister ZEROING_REGISTER_4 = new PointerRegister(Register.ACCUMULATOR, Immediate.C_TIMES_4);
    public static final PointerRegister ZEROING_REGISTER_2 = new PointerRegister(Register.ACCUMULATOR, Immediate.C_TIMES_2);
    public static final PointerRegister ZEROING_REGISTER_1 = new PointerRegister(Register.ACCUMULATOR, Register.COUNTER);

    public final InstructionArg arg;
    public final InstructionArg offsetArg;
    public final long offset;

    private PointerRegister(final InstructionArg arg, final InstructionArg offsetArg, final long offset){
        this.arg = arg;
        this.offsetArg = offsetArg;
        this.offset = offset;
    }

    public PointerRegister(final InstructionArg arg, final long offset){
        this(arg, null, offset);
    }

    public PointerRegister(final InstructionArg arg, final InstructionArg offset){
        this(arg, offset, 0);
    }

    public PointerRegister(final InstructionArg arg){
        this(arg, null, 0);
    }

    @Override
    public String getValue(final Size size) {
        if(offset == 0 && offsetArg == null) return "[" + arg.getValue() + "]";
        if(null != offsetArg) return "[" + arg.getValue() + " + " + offsetArg.getValue() + "]";
        return "[" + arg.getValue() + " + " + offset + "]";
    }
}
