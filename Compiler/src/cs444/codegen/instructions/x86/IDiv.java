package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public class IDiv implements X86Instruction {
    private final InstructionArg with;

    public IDiv(InstructionArg with){
        this.with = with;
    }

    @Override
    public String generate() {
        return "idiv " + with.getValue(Size.DWORD);
    }
}
