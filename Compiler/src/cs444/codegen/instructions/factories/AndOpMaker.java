package cs444.codegen.instructions.factories;

import cs444.codegen.Register;
import cs444.codegen.instructions.And;

public class AndOpMaker implements BinOpMaker {
    public static final AndOpMaker maker = new AndOpMaker();

    private AndOpMaker () { }

    @Override
    public And make(Register one, Register two) {
        return new And(one, two);
    }
}
