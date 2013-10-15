package cs444.codegen.x86.instructions;

import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;


public class Db extends UniInstruction{
    public Db(final Immediate data) {
        //Immediate won't need a sizeHelper
        super("db", data, Size.DWORD, null, 0, 0);
    }
}
