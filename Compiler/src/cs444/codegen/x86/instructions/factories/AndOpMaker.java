package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.And;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;


public class AndOpMaker implements BinOpMaker {
    public static final AndOpMaker maker = new AndOpMaker();

    private AndOpMaker () { }

    @Override
    public And make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new And(one, two, sizeHelper);
    }
}
