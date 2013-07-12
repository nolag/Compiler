package cs444.codegen.x86;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Memory extends InstructionArg{
    public static final Map<SizeHelper<X86Instruction, Size>, Memory> thisPointers =
            new HashMap<SizeHelper<X86Instruction, Size>, Memory>();

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

    public static Memory getThisPointer(final SizeHelper<X86Instruction, Size> sizeHelper){
        Memory pr = thisPointers.get(sizeHelper);
        if(pr == null){
            pr = new Memory(Register.FRAME, sizeHelper.getDefaultStackSize() * 2);
            thisPointers.put(sizeHelper, pr);
        }
        return pr;
    }

    @Override
    public String getValue(final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        if(offset == 0 && offsetArg == null) return "[" + arg.getValue(sizeHelper) + "]";
        if(null != offsetArg) return "[" + arg.getValue(sizeHelper) + " + " + offsetArg.getValue(sizeHelper) + "]";
        return "[" + arg.getValue(sizeHelper) + " + " + offset + "]";
    }
}
