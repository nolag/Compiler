package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.Register;

public class Xor extends BinInstruction {
    public Xor(final Register arg1, final NotMemory arg2, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("xor", arg1, arg2, sizeHelper, 1);
    }

    public Xor(final Register arg1, final Memory arg2, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("xor", arg1, arg2, sizeHelper, 2);
    }

    public Xor(final Memory arg1, final NotMemory arg2, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("xor", arg1, arg2, sizeHelper, 3);
    }
}
