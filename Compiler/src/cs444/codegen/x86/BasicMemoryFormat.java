package cs444.codegen.x86;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class BasicMemoryFormat extends MemoryFormat {
    private static final Map<Register, BasicMemoryFormat> map = new HashMap<>();

    public final NotMemory arg;

    public static BasicMemoryFormat getBasicMemoryFormat(final Register reg) {
        BasicMemoryFormat format = map.get(reg);
        if (format == null) {
            format = new BasicMemoryFormat(reg);
            map.put(reg, format);
        }

        return format;
    }

    private BasicMemoryFormat(final Register reg) {
        this.arg = reg;
    }

    public BasicMemoryFormat(final Immediate arg) {
        this.arg = arg;
    }

    @Override
    public String getValue(final SizeHelper<X86Instruction, Size> sizeHelper) {
        return arg.getValue(sizeHelper);
    }

    @Override
    public boolean uses(final InstructionArg<X86Instruction, ?> what) {
        return what == arg;
    }
}
