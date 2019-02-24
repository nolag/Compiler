package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

import java.util.HashMap;
import java.util.Map;

public class Push extends UniInstruction {
    private static final Map<SizeHelper<X86Instruction, Size>, Push> map = new HashMap<>();

    public Push(Register what, Size size, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, size, sizeHelper, 1, 1);
    }

    public Push(Register what, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, sizeHelper, 1, 1);
    }

    public Push(Immediate what, Size size, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, size, sizeHelper, 1, 3);
    }

    public Push(Immediate what, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, sizeHelper, 1, 3);
    }

    public Push(Memory what, Size size, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, size, sizeHelper, 4, 4);
    }

    public Push(Memory what, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("push", what, sizeHelper, 4, 4);
    }

    public static Push getStackPush(SizeHelper<X86Instruction, Size> sizeHelper) {
        Push push = map.get(sizeHelper);
        if (push == null) {
            push = new Push(Register.FRAME, sizeHelper);
            map.put(sizeHelper, push);
        }
        return push;
    }
}
