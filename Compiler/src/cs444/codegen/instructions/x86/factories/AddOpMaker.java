package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Add;
import cs444.codegen.x86.Register;

public class AddOpMaker implements BinOpMaker {
    public static final AddOpMaker maker = new AddOpMaker();

    private AddOpMaker() { }

    @Override
    public Add make(Register one, Register two) {
        return new Add(one, two);
    }

}
