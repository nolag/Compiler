package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Xchg extends BinInstruction {
    public Xchg(Register addTo, Register addWith, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("xchg", addTo, addWith, sizeHelper, 3, 2);
    }

    public Xchg(Register addTo, Memory addWith, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("xchg", addTo, addWith, sizeHelper, 3, 4);
    }

    public Xchg(Memory addTo, Register addWith, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("xchg", addTo, addWith, sizeHelper, 5, 4);
    }
}
