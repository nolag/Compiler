package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;

public class Pop implements Instruction {
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
        return "pop " + InstructionArg.getSizeStr(size) + ", " + what.getValue(size);
    }
}
