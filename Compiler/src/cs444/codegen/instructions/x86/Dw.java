package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;


public class Dw extends DataInstruction implements X86Instruction {
    public Dw(InstructionArg data) {
        super(data);
    }

    @Override
    public String generate() {
        return "dw " + data.getValue(Size.LOW);
    }

}
