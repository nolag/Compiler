package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.ShXd;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public interface ShXdMaker {
    ShXd make(Register reg, Register reg2, Immediate arg, SizeHelper<X86Instruction,
            Size> sizeHelper);

    ShXd make(Memory mem, Register reg2, Immediate arg, SizeHelper<X86Instruction,
            Size> sizeHelper);

    ShXd make(Register reg, Register reg2, Register reg3, SizeHelper<X86Instruction,
            Size> sizeHelper);

    ShXd make(Memory mem, Register reg2, Register reg3, SizeHelper<X86Instruction,
            Size> sizeHelper);
}
