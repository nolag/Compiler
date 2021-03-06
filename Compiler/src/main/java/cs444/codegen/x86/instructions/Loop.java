package cs444.codegen.x86.instructions;

import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Loop extends X86Instruction {
    private final String s;

    public Loop(String s) {
        //6 on loop 2 on no loop, assume most loop would round to 6
        super(6, 2);
        this.s = s;
    }

    @Override
    public String generate() {
        return "loop " + s;
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        return what == Register.COUNTER;
    }
}
