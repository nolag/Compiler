package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Or;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class OrOpMaker implements BinOpMaker {
    public static final OrOpMaker maker = new OrOpMaker();

    private OrOpMaker () { }

    @Override
    public Or make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Or(one, two, sizeHelper);
    }

    //Size won't matter, each bit is only with the same size anyways
    @Override
    public X86Instruction make(final Register one, final Register two, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Or(one, two, sizeHelper);
    }
    
    @Override
    public Or make(final Register one, final Immediate two, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Or(one, two, sizeHelper);
    }

    //Size won't matter, each bit is only with the same size anyways
    @Override
    public X86Instruction make(final Register one, final Immediate two, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Or(one, two, sizeHelper);
    }
}
