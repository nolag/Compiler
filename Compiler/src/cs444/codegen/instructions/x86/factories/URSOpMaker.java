package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Shr;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class URSOpMaker implements BinOpMaker {
    public static final URSOpMaker maker = new URSOpMaker();

    private URSOpMaker () { }

    @Override
    public Shr make(final Register one, final Register two, final X86SizeHelper sizeHelper){
        return new Shr(one, two, sizeHelper);
    }
}
