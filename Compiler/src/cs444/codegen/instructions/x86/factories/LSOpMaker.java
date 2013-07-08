package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Shl;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class LSOpMaker implements BinOpMaker {
    public static final LSOpMaker maker = new LSOpMaker();

    private LSOpMaker () { }

    @Override
    public Shl make(final Register one, final Register two, final X86SizeHelper sizeHelper){
        return new Shl(one, two, sizeHelper);
    }
}
