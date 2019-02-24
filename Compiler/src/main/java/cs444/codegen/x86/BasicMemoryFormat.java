package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

import java.util.HashMap;
import java.util.Map;

public class BasicMemoryFormat extends MemoryFormat {
    private static final Map<Register, BasicMemoryFormat> map = new HashMap<>();

    public final NotMemory arg;

    private BasicMemoryFormat(Register reg) {
        arg = reg;
    }

    public BasicMemoryFormat(Immediate arg) {
        this.arg = arg;
    }

    public static BasicMemoryFormat getBasicMemoryFormat(Register reg) {
        BasicMemoryFormat format = map.get(reg);
        if (format == null) {
            format = new BasicMemoryFormat(reg);
            map.put(reg, format);
        }

        return format;
    }

    @Override
    public String getValue(SizeHelper<X86Instruction, Size> sizeHelper) {
        return arg.getValue(sizeHelper);
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        return what == arg;
    }
}
