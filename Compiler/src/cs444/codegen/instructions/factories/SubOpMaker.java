package cs444.codegen.instructions.factories;

import cs444.codegen.Register;
import cs444.codegen.instructions.Sub;

public class SubOpMaker implements BinOpMaker {
    public static final SubOpMaker maker = new SubOpMaker();

    private SubOpMaker() { }

    @Override
    public Sub make(Register one, Register two) {
        return new Sub(one, two);
    }

}
