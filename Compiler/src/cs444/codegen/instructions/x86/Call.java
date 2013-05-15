package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public class Call implements X86Instruction {
    private final InstructionArg what;

    public Call(InstructionArg what){
        this.what = what;
    }

    @Override
    public String generate() {
        return "call " + what.getValue(Size.DWORD);
    }
}
