package cs444.codegen.instructions.x86.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.Add;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;


public class AddOpMaker implements BinOpMaker {
    public static final AddOpMaker maker = new AddOpMaker();

    private AddOpMaker() { }

    @Override
    public Add make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Add(one, two, sizeHelper);
    }
}
