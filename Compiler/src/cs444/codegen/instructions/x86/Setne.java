package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.SetInstruciton;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Setne extends SetInstruciton{
    public Setne(final Register arg, final X86SizeHelper sizeHelper){
        super("setne", arg, sizeHelper, 3);
    }

    public Setne(final Memory arg, final X86SizeHelper sizeHelper){
        super("setne", arg, sizeHelper, 4);
    }
}
