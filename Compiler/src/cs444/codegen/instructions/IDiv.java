package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;

public class IDiv implements Instruction {
    private final InstructionArg with;

    public IDiv(InstructionArg with){
        this.with = with;
    }

    @Override
    public String generate() {
        return "idiv  " + with.getValue(Size.DWORD);
    }
}
