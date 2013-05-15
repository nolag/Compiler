package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public class IMul implements X86Instruction {
    private final InstructionArg with;

    public IMul(InstructionArg with){
        this.with = with;
    }

    @Override
    public String generate() {
        return "imul " + with.getValue(Size.DWORD);
    }
}
