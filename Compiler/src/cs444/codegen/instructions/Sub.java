package cs444.codegen.instructions;

import cs444.codegen.InstructionArg.Size;
import cs444.codegen.Register;

public class Sub implements Instruction {
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
