package cs444.codegen.instructions.x86;

import cs444.codegen.x86.Register;
import cs444.codegen.x86.InstructionArg.Size;

public class Sub implements X86Instruction {
    public final Register minuend;
    public final Register subtrahend;

    public Sub(Register minuend, Register subtrahend){
        this.minuend = minuend;
        this.subtrahend = subtrahend;
    }

    @Override
    public String generate() {
        return "sub " + minuend.getValue(Size.DWORD) + ", " + subtrahend.getValue(Size.DWORD);
    }
}
