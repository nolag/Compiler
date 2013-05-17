package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;


public class Db extends UniInstruction{
    public Db(final Immediate data) {
        //Immediate won't need a sizeHelper
        super("db", data, Size.DWORD, null);
    }
}
