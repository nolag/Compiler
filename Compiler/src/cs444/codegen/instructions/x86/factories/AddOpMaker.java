package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Add;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class AddOpMaker implements BinOpMaker {
    public static final AddOpMaker maker = new AddOpMaker();

    private AddOpMaker() { }

    @Override
    public Add make(final Register one, final Register two, final X86SizeHelper sizeHelper){
        return new Add(one, two, sizeHelper);
    }
}
