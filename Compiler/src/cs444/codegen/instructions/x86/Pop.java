package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public class Pop implements X86Instruction {
    public final InstructionArg what;
    public final Size size;

    public Pop(InstructionArg what, Size size){
        this.what = what;
        this.size = size;
    }

    public Pop(InstructionArg what){
        this(what, Size.DWORD);
    }

    @Override
    public String generate() {
        return "pop " + InstructionArg.getSizeStr(size) + " " + what.getValue(size);
    }
}
