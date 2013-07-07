package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.X86SizeHelper;

public class IDiv extends UniInstruction{
    public IDiv(final NotMemory arg1, final X86SizeHelper sizeHelper){
        super("idiv", arg1, sizeHelper, 43);
    }

    public IDiv(final Memory arg1, final X86SizeHelper sizeHelper){
        super("idiv", arg1, sizeHelper, 44);
    }
}