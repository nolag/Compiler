package cs444.codegen.x86.instructions;

import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Leave extends X86Instruction {
    public static final Leave LEAVE = new Leave();

    private Leave() {
        super(5, 1);
    }

    @Override
    public String generate() {
        return "leave";
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        return false;
    }
}
