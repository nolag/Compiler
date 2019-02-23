package cs444.codegen.x86.x86_64.instructions;

import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Syscall extends X86Instruction {

    public Syscall() {
        //No idea the time, assume it's the same as int
        //No idea the space, but it can't be replaced anyways
        super(26, 1);
    }

    @Override
    public String generate() {
        return "syscall";
    }

    @Override
    public boolean uses(final InstructionArg<X86Instruction, ?> what) {
        return false;
    }
}
