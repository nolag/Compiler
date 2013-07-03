package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;


public class Dw extends UniInstruction{
    public Dw(final Immediate data) {
        //Immediate won't need a sizeHelper
        super("dw", data, Size.DWORD, null, 0);
    }
}
