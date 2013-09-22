package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Call extends UniInstruction {
    public Call(final Immediate what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("call", what, sizeHelper, 3);
    }

    public Call(final InstructionArg what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("call", what, sizeHelper, 5);
    }

    @Override
    public boolean writesTo(final InstructionArg what) {
        //native call may need to back up ebx
        return true;
    }
}
