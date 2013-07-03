package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Setl;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class SetlMaker implements UniOpMaker {
    public static SetlMaker maker = new SetlMaker();

    private SetlMaker(){ }

    @Override
    public Setl make(final Register arg, final X86SizeHelper sizeHelper) {
        return new Setl(arg, sizeHelper);
    }
}
