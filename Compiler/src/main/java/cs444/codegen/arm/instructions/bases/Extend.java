package cs444.codegen.arm.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.instructions.InstructionArg;

public abstract class Extend extends ArmInstruction {
    public final Register src;
    public final Register dst;
    public final SizeHelper<ArmInstruction, Size> sizeHelper;
    private final String name;

    protected Extend(String name, Register dst, Register src, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        super(1);
        this.name = name;
        this.dst = dst;
        this.src = src;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dst.uses(what) || src.uses(what);
    }

    @Override
    public String generate() {
        return name + " " + dst.getValue(sizeHelper) + ", " + src.getValue(sizeHelper);
    }
}
