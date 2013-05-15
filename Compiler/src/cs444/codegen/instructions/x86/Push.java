package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.InstructionArg.Size;

public class Push implements X86Instruction {
    public final InstructionArg what;
    public final Size size;

    public static final Push STACK_FRAME = new Push(Register.FRAME);

    public Push(InstructionArg what, Size size){
        this.what = what;
        this.size = size;
    }

    public Push(InstructionArg what){
        this(what, Size.DWORD);
    }

    @Override
    public String generate() {
        return "push " + InstructionArg.getSizeStr(size) + " " + what.getValue(size);
    }

}
