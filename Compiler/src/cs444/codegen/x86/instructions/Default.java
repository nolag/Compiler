package cs444.codegen.x86.instructions;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Default extends X86Instruction {
    public static Default rel = new Default("rel");

    private final String what;

    private Default(final String what) {
        super(0, 0);
        this.what = what;
    }

    @Override
    public boolean uses(final InstructionArg what) {
        return false;
    }

    @Override
    public String generate() {
        return "default " + what;
    }
}
