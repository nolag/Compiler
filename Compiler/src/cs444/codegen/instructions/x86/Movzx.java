package cs444.codegen.instructions.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.MovX;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;


public class Movzx extends MovX{
    public Movzx(final Register dst, final InstructionArg src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("movzx", dst, src, size, sizeHelper);
    }
}
