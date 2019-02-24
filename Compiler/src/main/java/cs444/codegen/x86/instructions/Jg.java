package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Jg extends UniInstruction {
    public Jg(Immediate arg1, SizeHelper<X86Instruction, Size> sizeHelper) {
        //1 w/o jump 3 with ~= 2
        //2 short, 4 long
        super("jg", arg1, sizeHelper, 2, 4);
    }
}
