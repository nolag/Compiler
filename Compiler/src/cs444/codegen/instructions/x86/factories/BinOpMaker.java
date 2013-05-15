package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.X86Instruction;
import cs444.codegen.x86.Register;

public interface BinOpMaker {
    public X86Instruction make(Register one, Register two);
}
