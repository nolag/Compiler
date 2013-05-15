package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Sub;
import cs444.codegen.x86.Register;

public class SubOpMaker implements BinOpMaker {
    public static final SubOpMaker maker = new SubOpMaker();

    private SubOpMaker() { }

    @Override
    public Sub make(Register one, Register two) {
        return new Sub(one, two);
    }

}
