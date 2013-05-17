package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Sub;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class SubOpMaker implements BinOpMaker {
    public static final SubOpMaker maker = new SubOpMaker();

    private SubOpMaker() { }

    @Override
    public Sub make(final Register one, final Register two, final X86SizeHelper sizeHelper){
        return new Sub(one, two, sizeHelper);
    }

}
