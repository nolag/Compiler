package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.SetInstruciton;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Setge extends SetInstruciton{
    public Setge(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("setge", arg, sizeHelper, 3, 3);
    }

    public Setge(final Memory arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("setge", arg, sizeHelper, 4, 3);
    }
}
