package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Or;
import cs444.codegen.x86.Register;

public class OrOpMaker implements BinOpMaker {
    public static final OrOpMaker maker = new OrOpMaker();

    private OrOpMaker () { }

    @Override
    public Or make(Register one, Register two) {
        return new Or(one, two);
    }
}
