package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.SetInstruciton;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Setl extends SetInstruciton{
    public Setl(final Register arg, final X86SizeHelper sizeHelper){
        super("setl", arg, sizeHelper, 3);
    }

    public Setl(final Memory arg, final X86SizeHelper sizeHelper){
        super("setl", arg, sizeHelper, 4);
    }
}
