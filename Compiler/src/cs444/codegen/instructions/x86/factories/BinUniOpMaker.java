package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.X86Instruction;
import cs444.codegen.x86.InstructionArg;

public interface BinUniOpMaker {
    public X86Instruction make(InstructionArg arg);
}
