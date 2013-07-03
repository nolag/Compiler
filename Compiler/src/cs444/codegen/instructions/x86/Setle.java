package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.SetInstruciton;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Setle extends SetInstruciton{
    public Setle(final Register arg, final X86SizeHelper sizeHelper){
        super("setle", arg, sizeHelper, 3);
    }

    public Setle(final Memory arg, final X86SizeHelper sizeHelper){
        super("setle", arg, sizeHelper, 4);
    }
}
