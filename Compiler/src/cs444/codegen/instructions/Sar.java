package cs444.codegen.instructions;

import cs444.codegen.Immediate;
import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;
import cs444.codegen.Register;

public class Sar implements Instruction{
    public static final Sar acc32 = new Sar(Register.ACCUMULATOR, new Immediate("32"));

    private final Register reg;
    private final InstructionArg arg;

    public Sar(Register reg, InstructionArg arg){
        this.reg = reg;
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "sar " + reg.get32() + ", " + arg.getValue(Size.DWORD);
    }
}
