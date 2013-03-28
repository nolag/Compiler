package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;

public class Dd implements Instruction {
    private final InstructionArg data;

    public Dd(InstructionArg data) {
        this.data = data;
    }

    @Override
    public String generate() {
        return "dd " + data.getValue(Size.DWORD);
    }

}
