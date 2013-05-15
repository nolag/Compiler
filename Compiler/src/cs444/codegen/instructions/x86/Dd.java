package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;


public class Dd extends DataInstruction implements X86Instruction {
    public Dd(InstructionArg data) {
        super(data);
    }

    @Override
    public String generate() {
        return "dd " + data.getValue(Size.DWORD);
    }

}
