package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.SetInstruciton;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Sete extends SetInstruciton {
    public Sete(Register arg, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sete", arg, sizeHelper, 3, 3);
    }

    public Sete(Memory arg, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sete", arg, sizeHelper, 4, 3);
    }
}
