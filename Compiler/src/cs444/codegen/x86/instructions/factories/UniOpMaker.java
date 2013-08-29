package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;


public interface UniOpMaker {
    public X86Instruction make(Register arg, SizeHelper<X86Instruction, Size> sizeHelper);
}