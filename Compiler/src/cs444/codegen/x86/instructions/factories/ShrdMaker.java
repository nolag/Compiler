package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Shrd;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class ShrdMaker implements ShXdMaker {

    public static ShrdMaker maker = new ShrdMaker();

    private ShrdMaker() { }

    @Override
    public Shrd make(final Register reg, final Register reg2, final Immediate arg, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shrd(reg, reg2, arg, sizeHelper);
    }

    @Override
    public Shrd make(final Memory mem, final Register reg2, final Immediate arg, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shrd(mem, reg2, arg, sizeHelper);
    }

    @Override
    public Shrd make(final Register reg, final Register reg2, final Register reg3, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shrd(reg, reg2, reg3, sizeHelper);
    }

    @Override
    public Shrd make(final Memory mem, final Register reg2, final Register reg3, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shrd(mem, reg2, reg3, sizeHelper);
    }
}
