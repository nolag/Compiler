package cs444.codegen.x86.instructions;

import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Ret extends X86Instruction {
    public static final Ret RET = new Ret();

    private Ret(){
        super(5, 1);
    }

    @Override
    public String generate() {
        return "ret";
    }

    @Override
    public boolean uses(final InstructionArg<X86Instruction, ?> what) {
        return false;
    }
}
