package cs444.codegen.x86;

import java.util.HashMap;
import java.util.Map;


public class PointerRegister extends InstructionArg{
    public static final Map<X86SizeHelper, PointerRegister> thisPointers = new HashMap<X86SizeHelper, PointerRegister>();

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

    public static PointerRegister getThisPointer(final X86SizeHelper sizeHelper){
        PointerRegister pr = thisPointers.get(sizeHelper);
        if(pr == null){
            pr = new PointerRegister(Register.FRAME, sizeHelper.getDefaultStackSize() * 2);
            thisPointers.put(sizeHelper, pr);
        }
        return pr;
    }

    @Override
    public String getValue(final Size size, final X86SizeHelper sizeHelper) {
        if(offset == 0 && offsetArg == null) return "[" + arg.getValue(sizeHelper) + "]";
        if(null != offsetArg) return "[" + arg.getValue(sizeHelper) + " + " + offsetArg.getValue(sizeHelper) + "]";
        return "[" + arg.getValue(sizeHelper) + " + " + offset + "]";
    }
}
