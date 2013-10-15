package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Jmp extends UniInstruction{
    public Jmp(final Immediate arg1, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("jmp", arg1, sizeHelper, 3, 4);
    }
}
