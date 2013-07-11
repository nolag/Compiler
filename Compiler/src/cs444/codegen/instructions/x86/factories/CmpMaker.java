package cs444.codegen.instructions.x86.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.Cmp;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;


public class CmpMaker implements BinOpMaker{
    public static CmpMaker maker = new CmpMaker();

    private CmpMaker(){ }

    @Override
    public Cmp make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Cmp(one, two, sizeHelper);
    }

}
