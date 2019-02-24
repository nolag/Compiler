package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.MovX;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Movsx extends MovX {
    public Movsx(Register dst, InstructionArg<X86Instruction, Size> src, Size size,
                 SizeHelper<X86Instruction, Size> sizeHelper) {
        super("movsx", dst, src, size, sizeHelper);
    }
}
