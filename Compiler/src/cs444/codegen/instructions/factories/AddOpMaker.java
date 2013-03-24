package cs444.codegen.instructions.factories;

import cs444.codegen.Register;
import cs444.codegen.instructions.Add;

public class AddOpMaker implements BinOpMaker {
    public static final AddOpMaker maker = new AddOpMaker();

    private AddOpMaker() { }

    @Override
    public Add make(Register one, Register two) {
        return new Add(one, two);
    }

}
