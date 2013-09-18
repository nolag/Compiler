package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class IDiv extends UniInstruction {
    public IDiv(final NotMemory arg1, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("idiv", arg1, sizeHelper, 43);
    }

    public IDiv(final NotMemory arg1, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("idiv", arg1, size, sizeHelper, 43);
    }

    public IDiv(final Memory arg1, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("idiv", arg1, sizeHelper, 44);
    }

    public IDiv(final Memory arg1, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("idiv", arg1, size, sizeHelper, 44);
    }
}
