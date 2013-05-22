package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.X86SizeHelper;

public class Jg extends UniInstruction{
    public Jg(final Immediate arg1, final X86SizeHelper sizeHelper){
      //1 w/o jump 3 with ~= 2
        super("jg", arg1, sizeHelper, 2);
    }
}
