package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate12;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Str;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class StrMaker implements Imm12OrRegMaker {
    public static final StrMaker instance = new StrMaker();

    private StrMaker() {}

    @Override
    public Str make(Register dest, Register r1, Register r2, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Str(dest, r1, r2, sizeHelper);
    }

    @Override
    public Str make(Register dest, Register r1, Immediate12 imm, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Str(dest, r1, imm, sizeHelper);
    }
}
