package cs444.codegen.x86.x86_64.instructions;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Syscall extends X86Instruction{

    public Syscall() {
        //No idea the cost, assume it's the same as int
        super(26);
    }

    @Override
    public String generate() {
        return "syscall";
    }

    @Override
    public boolean writesTo(final InstructionArg what) {
        return false;
    }
}
