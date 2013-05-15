package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.InstructionArg.Size;

public class Sar implements X86Instruction{
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
