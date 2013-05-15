package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Cmp;
import cs444.codegen.x86.Register;

public class CmpMaker implements BinOpMaker{
    public static CmpMaker maker = new CmpMaker();

    private CmpMaker(){ }

    @Override
    public Cmp make(Register one, Register two) {
        return new Cmp(one, two);
    }

}
