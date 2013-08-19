package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.ShXd;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public interface ShXdMaker {
    public ShXd make(final Register reg, final Register reg2, final Immediate arg, final SizeHelper<X86Instruction, Size> sizeHelper);
    public ShXd make(final Memory mem, final Register reg2, final Immediate arg, final SizeHelper<X86Instruction, Size> sizeHelper);
    public ShXd make(final Register reg, final Register reg2, final Register reg3, final SizeHelper<X86Instruction, Size> sizeHelper);
    public ShXd make(final Memory mem, final Register reg2, final Register reg3, final SizeHelper<X86Instruction, Size> sizeHelper);
}
