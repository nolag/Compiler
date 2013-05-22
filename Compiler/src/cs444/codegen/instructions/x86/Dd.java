package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;

public class Dd extends UniInstruction{
    public Dd(final Immediate data) {
        //Immediate won't need a sizeHelper
        super("dd", data, Size.DWORD, null, 0);
    }
}
