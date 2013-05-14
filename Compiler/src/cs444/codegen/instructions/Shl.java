package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.Register;
import cs444.codegen.InstructionArg.Size;

public class Shl implements Instruction{
    private final Register reg;
    private final InstructionArg arg;

    public Shl(Register reg, InstructionArg arg){
        this.reg = reg;
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "shl " + reg.get32() + ", " + arg.getValue(Size.DWORD);
    }
}
