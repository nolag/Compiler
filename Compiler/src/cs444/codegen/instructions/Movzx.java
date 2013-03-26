package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;
import cs444.codegen.Register;

public class Movzx implements Instruction{

    public final Register dst;
    public final InstructionArg src;
    public final Size size;

    //NOTE register register is legal, but I don't see why we would want it.
    public Movzx(Register dst, InstructionArg src, Size size){
        this.dst = dst;
        this.src = src;
        this.size = size;
    }

    @Override
    public String generate() {
        String sizeStr = null;

        switch(size){
        case LOW:
        case HIGH:
            sizeStr = "byte";
            break;
        case WORD:
            sizeStr = "word";
        case DWORD:
            sizeStr = "dword";
        }

        return "movzx " + dst.getValue(Size.DWORD) + ", " + sizeStr + " " + src.getValue(size);
    }
}