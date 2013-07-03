package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Or;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class OrOpMaker implements BinOpMaker {
    public static final OrOpMaker maker = new OrOpMaker();

    private OrOpMaker () { }

    @Override
    public Or make(final Register one, final Register two, final X86SizeHelper sizeHelper){
        return new Or(one, two, sizeHelper);
    }
}
