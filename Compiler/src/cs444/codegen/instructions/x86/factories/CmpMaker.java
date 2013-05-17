package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Cmp;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class CmpMaker implements BinOpMaker{
    public static CmpMaker maker = new CmpMaker();

    private CmpMaker(){ }

    @Override
    public Cmp make(final Register one, final Register two, final X86SizeHelper sizeHelper){
        return new Cmp(one, two, sizeHelper);
    }

}
