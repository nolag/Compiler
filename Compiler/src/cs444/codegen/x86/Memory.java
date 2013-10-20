package cs444.codegen.x86;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Memory extends InstructionArg {
    public static final Map<SizeHelper<X86Instruction, Size>, Memory> thisPointers =
            new HashMap<SizeHelper<X86Instruction, Size>, Memory>();

    public final MemoryFormat format;


    public Memory(final MemoryFormat format){
        this.format = format;
    }

    public static Memory getThisPointer(final SizeHelper<X86Instruction, Size> sizeHelper){
        Memory pr = thisPointers.get(sizeHelper);
        if(pr == null){
            pr = new Memory(new AddMemoryFormat(Register.FRAME, new Immediate (sizeHelper.getDefaultStackSize() * 2)));
            thisPointers.put(sizeHelper, pr);
        }
        return pr;
    }

    @Override
    public String getValue(final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return format.getMemoryValue(sizeHelper);
    }

    @Override
    public boolean uses(final InstructionArg what) {
        return format.uses(what);
    }
}
