package cs444.codegen.instructions.factories;

import cs444.codegen.Register;
import cs444.codegen.instructions.Cmp;

public class CmpMaker implements BinOpMaker{
    public static CmpMaker maker = new CmpMaker();

    private CmpMaker(){ }

    @Override
    public Cmp make(Register one, Register two) {
        return new Cmp(one, two);
    }

}
