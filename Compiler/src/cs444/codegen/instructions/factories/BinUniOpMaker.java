package cs444.codegen.instructions.factories;

import cs444.codegen.InstructionArg;
import cs444.codegen.instructions.Instruction;

public interface BinUniOpMaker {
    public Instruction make(InstructionArg arg);
}
