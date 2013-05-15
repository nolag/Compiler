package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.And;
import cs444.codegen.x86.Register;

public class AndOpMaker implements BinOpMaker {
    public static final AndOpMaker maker = new AndOpMaker();

    private AndOpMaker () { }

    @Override
    public And make(Register one, Register two) {
        return new And(one, two);
    }
}
