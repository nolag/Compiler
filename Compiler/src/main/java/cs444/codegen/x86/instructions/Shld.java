package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.ShXd;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Shld extends ShXd {
    public Shld(Register reg, Register reg2, Immediate arg,
                SizeHelper<X86Instruction, Size> sizeHelper) {
        super("shld", reg, reg2, arg, sizeHelper, 2, 4);
    }

    public Shld(Memory mem, Register reg2, Immediate arg,
                SizeHelper<X86Instruction, Size> sizeHelper) {
        super("shld", mem, reg2, arg, sizeHelper, 3, 6);
    }

    public Shld(Register reg, Register reg2, Register reg3,
                SizeHelper<X86Instruction, Size> sizeHelper) {
        super("shld", reg, reg2, reg3, sizeHelper, 3, 3);
    }

    public Shld(Memory mem, Register reg2, Register reg3,
                SizeHelper<X86Instruction, Size> sizeHelper) {
        super("shld", mem, reg2, reg3, sizeHelper, 4, 5);
    }
}
