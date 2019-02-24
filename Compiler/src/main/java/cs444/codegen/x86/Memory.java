package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

import java.util.HashMap;
import java.util.Map;

public class Memory extends InstructionArg<X86Instruction, Size> {
    public static final Map<SizeHelper<X86Instruction, Size>, Memory> thisPointers =
            new HashMap<SizeHelper<X86Instruction, Size>, Memory>();

    public final MemoryFormat format;

    public Memory(MemoryFormat format) {
        this.format = format;
    }

    public static Memory getThisPointer(SizeHelper<X86Instruction, Size> sizeHelper) {
        Memory pr = thisPointers.get(sizeHelper);
        if (pr == null) {
            pr = new Memory(new AddMemoryFormat(Register.FRAME, new Immediate(sizeHelper.getDefaultStackSize() * 2)));
            thisPointers.put(sizeHelper, pr);
        }
        return pr;
    }

    @Override
    public String getValue(Size size, SizeHelper<X86Instruction, Size> sizeHelper) {
        return format.getMemoryValue(sizeHelper);
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        return format.uses(what);
    }
}
