package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.MovX;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Movsx extends MovX {
    public Movsx(final Register dst, final InstructionArg src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("movsx", dst, src, size, sizeHelper);
    }
}
