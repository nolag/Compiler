package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Call extends UniInstruction {
    //can't find size, but it's not like it can easily be replaced
    public Call(final Immediate what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("call", what, sizeHelper, 3, 1);
    }

    public Call(final InstructionArg<X86Instruction, Size> what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("call", what, sizeHelper, 5, 1);
    }

    @Override
    public boolean uses(final InstructionArg<X86Instruction, ?> what) {
        //native call may need to back up ebx
        return true;
    }
}
