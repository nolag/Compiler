package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Or extends BinInstruction {
    public Or(Register addTo, Register addWith, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("or", addTo, addWith, sizeHelper, 1, 2);
    }

    public Or(Register addTo, Immediate addWith, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("or", addTo, addWith, sizeHelper, 1, addTo == Register.ACCUMULATOR ? 3 : 4);
    }

    public Or(Register addTo, Memory addWith, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("or", addTo, addWith, sizeHelper, 2, 4);
    }

    public Or(Memory addTo, NotMemory addWith, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("or", addTo, addWith, sizeHelper, 3, 4);
    }
}
