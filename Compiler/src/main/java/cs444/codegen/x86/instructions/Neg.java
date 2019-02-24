package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Neg extends UniInstruction {
    public Neg(Memory what, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("neg", what, sizeHelper, 3, 4);
    }

    public Neg(Register what, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("neg", what, sizeHelper, 1, 2);
    }
}
