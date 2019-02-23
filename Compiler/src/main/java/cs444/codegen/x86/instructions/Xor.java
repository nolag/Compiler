package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Xor extends BinInstruction {
    public Xor(final Register arg1, final Register arg2, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("xor", arg1, arg2, sizeHelper, 1, 2);
    }

    public Xor(final Register arg1, final Immediate arg2, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("xor", arg1, arg2, sizeHelper, 1, 4);
    }

    public Xor(final Register arg1, final Memory arg2, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("xor", arg1, arg2, sizeHelper, 2, 4);
    }

    public Xor(final Memory arg1, final Register arg2, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("xor", arg1, arg2, sizeHelper, 3, 4);
    }

    public Xor(final Memory arg1, final Immediate arg2, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("xor", arg1, arg2, sizeHelper, 3, 6);
    }
}
