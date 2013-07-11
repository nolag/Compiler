package cs444.codegen.instructions.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;


public class Call extends UniInstruction {
    public Call(final Immediate what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("call", what, sizeHelper, 3);
    }

    public Call(final InstructionArg what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("call", what, sizeHelper, 5);
    }
}
