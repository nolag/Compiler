package cs444.codegen.x86.instructions;

import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;

public class Dd extends UniInstruction{
    public Dd(final Immediate ... data) {
        //Immediate won't need a sizeHelper
        super("dd", data, Size.DWORD, null, 0, 0);
    }
}
