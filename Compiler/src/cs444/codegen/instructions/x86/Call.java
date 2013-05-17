package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.X86SizeHelper;

public class Call implements X86Instruction {
    public final InstructionArg what;
    public final X86SizeHelper sizeHelper;

    public Call(final InstructionArg what, final X86SizeHelper sizeHelper){
        this.what = what;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public String generate() {
        return "call " + what.getValue(Size.DWORD, sizeHelper);
    }
}
