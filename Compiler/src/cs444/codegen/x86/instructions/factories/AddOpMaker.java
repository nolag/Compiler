package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Add;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class AddOpMaker implements BinOpMaker {
    public static final AddOpMaker maker = new AddOpMaker();

    private AddOpMaker() { }

    @Override
    public Add make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Add(one, two, sizeHelper);
    }

    @Override
    public Add make(final Register one, final Register two, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Add(one, two, size, sizeHelper);
    }
    
    @Override
    public Add make(final Register one, final Immediate two, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Add(one, two, sizeHelper);
    }

    @Override
    public Add make(final Register one, final Immediate two, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Add(one, two, size, sizeHelper);
    }
}
