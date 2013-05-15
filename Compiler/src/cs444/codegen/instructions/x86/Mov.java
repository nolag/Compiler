package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public class Mov implements X86Instruction{

    private final InstructionArg src;
    private final InstructionArg dest;
    private final Size size;

    public Mov(InstructionArg dest, InstructionArg src, Size size){
        this.src = src;
        this.dest = dest;
        this.size = size;
    }

    public Mov(InstructionArg dest, InstructionArg src){
        this(dest, src, Size.DWORD);
    }

    @Override
    public String generate() {
        return "mov " + InstructionArg.getSizeStr(size) + " " + dest.getValue(size) + ", " + src.getValue(size);
    }
}
