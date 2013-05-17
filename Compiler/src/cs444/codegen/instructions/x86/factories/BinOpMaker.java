package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public interface BinOpMaker {
    public X86Instruction make(Register one, Register two, X86SizeHelper sizeHelper);
}
