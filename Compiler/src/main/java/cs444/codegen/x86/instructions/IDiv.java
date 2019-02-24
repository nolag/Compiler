package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class IDiv extends UniInstruction {
    public IDiv(Register arg1, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("idiv", arg1, sizeHelper, 43, 2);
    }

    public IDiv(Register arg1, Size size, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("idiv", arg1, size, sizeHelper, 43, 2);
    }

    public IDiv(Memory arg1, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("idiv", arg1, sizeHelper, 44, 4);
    }

    public IDiv(Memory arg1, Size size, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("idiv", arg1, size, sizeHelper, 44, 4);
    }
}
