package cs444.codegen.x86.instructions;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Cdq extends X86Instruction{
    public static final Cdq cdq = new Cdq();

    private Cdq(){
        super(3);
    }

    @Override
    public String generate() {
        return "cdq";
    }

    @Override
    public final boolean writesTo(final InstructionArg what) {
        return what == Register.ACCUMULATOR || what == Register.DATA;
    }
}
