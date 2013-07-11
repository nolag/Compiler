package cs444.codegen.instructions.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;


public class Jmp extends UniInstruction{
    public Jmp(final Immediate arg1, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("jmp", arg1, sizeHelper, 3);
    }
}
