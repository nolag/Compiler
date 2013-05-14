package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.Register;
import cs444.codegen.InstructionArg.Size;

public class Push implements Instruction {
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
