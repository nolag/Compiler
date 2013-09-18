package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class And extends BinInstruction {
    public And(final Register addTo, final InstructionArg addWith, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("and", addTo, addWith, sizeHelper, 1);
    }

    public And(final Memory addTo, final NotMemory addWith, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("and", addTo, addWith, sizeHelper, 3);
    }
}
