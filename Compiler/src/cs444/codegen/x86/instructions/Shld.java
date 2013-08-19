package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.ShXd;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Shld extends ShXd{
    public Shld(final Register reg, final Register reg2, final Immediate arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shld", reg, reg2, arg, sizeHelper, 2);
    }

    public Shld(final Memory mem, final Register reg2, final Immediate arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shld", mem, reg2, arg, sizeHelper, 3);
    }

    public Shld(final Register reg, final Register reg2, final Register reg3, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shld", reg, reg2, reg3, sizeHelper, 3);
    }

    public Shld(final Memory mem, final Register reg2, final Register reg3, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shld", mem, reg2, reg3, sizeHelper, 4);
    }
}
