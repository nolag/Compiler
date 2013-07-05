package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Sar;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class RSOpMaker implements BinOpMaker {
    public static final RSOpMaker maker = new RSOpMaker();

    private RSOpMaker () { }

    @Override
    public Sar make(final Register one, final Register two, final X86SizeHelper sizeHelper){
        return new Sar(one, two, sizeHelper);
    }
}
