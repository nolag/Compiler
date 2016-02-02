package cs444.codegen.x86.instructions;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Push extends UniInstruction {
    private static final Map<SizeHelper<X86Instruction, Size>, Push> map = new HashMap<>();

    public Push(final Register what, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, size, sizeHelper, 1, 1);
    }

    public Push(final Register what, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, sizeHelper, 1, 1);
    }

    public Push(final Immediate what, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, size, sizeHelper, 1, 3);
    }

    public Push(final Immediate what, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, sizeHelper, 1, 3);
    }

    public Push(final Memory what, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, size, sizeHelper, 4, 4);
    }

    public Push(final Memory what, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, sizeHelper, 4, 4);
    }

    public static Push getStackPush(final SizeHelper<X86Instruction, Size> sizeHelper) {
        Push push = map.get(sizeHelper);
        if (push == null) {
            push = new Push(Register.FRAME, sizeHelper);
            map.put(sizeHelper, push);
        }
        return push;
    }
}
