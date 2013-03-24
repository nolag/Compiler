package cs444.codegen.instructions.factories;

import cs444.codegen.Register;
import cs444.codegen.instructions.Or;

public class OrOpMaker implements BinOpMaker {
    public static final OrOpMaker maker = new OrOpMaker();

    private OrOpMaker () { }

    @Override
    public Or make(Register one, Register two) {
        return new Or(one, two);
    }
}
