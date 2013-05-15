package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;


public class Dq extends DataInstruction implements X86Instruction {
    public Dq(final InstructionArg data) {
        super(data);
    }

    @Override
    public String generate() {
        return "dq " + data.getValue(Size.QWORD);
    }

}
