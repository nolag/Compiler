package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.InstructionArg.Size;

public class Shl implements X86Instruction{
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
