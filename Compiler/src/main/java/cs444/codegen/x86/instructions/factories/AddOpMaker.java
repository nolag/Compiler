package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Add;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class AddOpMaker implements BinOpMaker {
    public static final AddOpMaker maker = new AddOpMaker();

    private AddOpMaker() { }

    @Override
    public Add make(Register one, Register two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Add(one, two, sizeHelper);
    }

    @Override
    public Add make(Register one, Register two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Add(one, two, size, sizeHelper);
    }

    @Override
    public Add make(Register one, Immediate two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Add(one, two, sizeHelper);
    }

    @Override
    public Add make(Register one, Immediate two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Add(one, two, size, sizeHelper);
    }
}
