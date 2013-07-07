package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public interface UniOpMaker {
    public X86Instruction make(Register arg, X86SizeHelper sizeHelper);
}