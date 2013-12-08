package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Lea extends BinInstruction {
    public Lea(final Register reg, final Memory mem, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("lea", reg, mem, sizeHelper, 1, 4);
    }
}