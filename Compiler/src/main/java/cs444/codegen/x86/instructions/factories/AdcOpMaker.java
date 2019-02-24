package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Adc;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class AdcOpMaker implements BinOpMaker {
    public static final AdcOpMaker maker = new AdcOpMaker();

    private AdcOpMaker() { }

    @Override
    public Adc make(Register one, Register two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Adc(one, two, sizeHelper);
    }

    @Override
    public Adc make(Register one, Register two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Adc(one, two, size, sizeHelper);
    }

    @Override
    public Adc make(Register one, Immediate two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Adc(one, two, sizeHelper);
    }

    @Override
    public Adc make(Register one, Immediate two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Adc(one, two, size, sizeHelper);
    }
}
