package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Bsr extends X86Instruction {
    private final Register src;
    private final Register dest;
    private final Size size;
    private final SizeHelper<X86Instruction, Size> sizeHelper;

    public Bsr(Register dest, Register src, Size size, SizeHelper<X86Instruction, Size> sizeHelper) {
        // Note: I'm not sure where I found the timing before, used the first result looks unfamiliar...
        // Space was from mov's register version, not sure where to find this anymore.
        super(3, 2);
        this.src = src;
        this.dest = dest;
        this.size = size;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        return what.uses(src) || what.uses(dest);
    }

    @Override
    public String generate() {
        return "bsr " + Size.getSizeStr(size) + " " + dest.getValue(size, sizeHelper)
                + ", " + src.getValue(size, sizeHelper);
    }
}
