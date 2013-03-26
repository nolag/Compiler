package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;

public class Call implements Instruction {
    private final InstructionArg what;

    public Call(InstructionArg what){
        this.what = what;
    }

    @Override
    public String generate() {
        return "call " + what.getValue(Size.DWORD);
    }
}
