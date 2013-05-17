package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Movzx extends BinInstruction{
    public Movzx(final Register dst, final InstructionArg src, final Size size, final X86SizeHelper sizeHelper){
        super("movsx", dst, src, sizeHelper, size);
    }
}
