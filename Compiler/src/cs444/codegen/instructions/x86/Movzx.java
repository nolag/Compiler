package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Movzx extends MovX{
    public Movzx(final Register dst, final InstructionArg src, final Size size, final X86SizeHelper sizeHelper){
        super("movzx", dst, src, size, sizeHelper);
    }
}
