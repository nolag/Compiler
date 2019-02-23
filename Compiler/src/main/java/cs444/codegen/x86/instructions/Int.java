package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Int extends UniInstruction{
    public Int(final Immediate arg1, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("int", arg1, sizeHelper, 26, 1);
    }
}
