package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Or;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;


public class OrOpMaker implements BinOpMaker {
    public static final OrOpMaker maker = new OrOpMaker();

    private OrOpMaker () { }

    @Override
    public Or make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Or(one, two, sizeHelper);
    }
}