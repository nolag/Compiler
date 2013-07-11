package cs444.codegen.instructions.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;


public class Int extends UniInstruction{
    public Int(final Immediate arg1, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("int", arg1, sizeHelper, 26);
    }
}
