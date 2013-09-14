package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public interface BinOpMaker {
    public X86Instruction make(Register one, Register two, SizeHelper<X86Instruction, Size> sizeHelper);
    public X86Instruction make(Register one, Register two, Size size, SizeHelper<X86Instruction, Size> sizeHelper);
}
