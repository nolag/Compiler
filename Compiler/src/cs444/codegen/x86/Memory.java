package cs444.codegen.x86;

import java.util.HashMap;
import java.util.Map;


public class Memory extends InstructionArg{
    public static final Map<X86SizeHelper, Memory> thisPointers = new HashMap<X86SizeHelper, Memory>();

    public final InstructionArg arg;
    public final InstructionArg offsetArg;
    public final long offset;

    private Memory(final InstructionArg arg, final InstructionArg offsetArg, final long offset){
        this.arg = arg;
        this.offsetArg = offsetArg;
        this.offset = offset;
    }

    public Memory(final InstructionArg arg, final long offset){
        this(arg, null, offset);
    }

    public Memory(final InstructionArg arg, final InstructionArg offset){
        this(arg, offset, 0);
    }

    public Memory(final InstructionArg arg){
        this(arg, null, 0);
    }

    public static Memory getThisPointer(final X86SizeHelper sizeHelper){
        Memory pr = thisPointers.get(sizeHelper);
        if(pr == null){
            pr = new Memory(Register.FRAME, sizeHelper.getDefaultStackSize() * 2);
            thisPointers.put(sizeHelper, pr);
        }
        return pr;
    }

    @Override
    public String getValue(final Size size, final X86SizeHelper sizeHelper) {
        String val;
        if(offset == 0 && offsetArg == null) val = "[" + arg.getValue(sizeHelper) + "]";
        else if(null != offsetArg) val = "[" + arg.getValue(sizeHelper) + " + " + offsetArg.getValue(sizeHelper) + "]";
        else val = "[" + arg.getValue(sizeHelper) + " + " + offset + "]";
        return X86SizeHelper.getStrVal(size) + " " + val;
    }
}
