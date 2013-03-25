package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;

public class IMul implements Instruction {
    private final InstructionArg with;

    public IMul(InstructionArg with){
        this.with = with;
    }

    @Override
    public String generate() {
        return "imul " + with.getValue(Size.DWORD);
    }
}
