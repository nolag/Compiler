package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Adc;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class AdcOpMaker implements BinOpMaker {
    public static final AdcOpMaker maker = new AdcOpMaker();

    private AdcOpMaker() { }

    @Override
    public Adc make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Adc(one, two, sizeHelper);
    }
}
