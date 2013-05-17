package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.And;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class AndOpMaker implements BinOpMaker {
    public static final AndOpMaker maker = new AndOpMaker();

    private AndOpMaker () { }

    @Override
    public And make(final Register one, final Register two, final X86SizeHelper sizeHelper){
        return new And(one, two, sizeHelper);
    }
}
