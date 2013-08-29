package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.SetInstruciton;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;


public class Setl extends SetInstruciton{
    public Setl(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("setl", arg, sizeHelper, 3);
    }

    public Setl(final Memory arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("setl", arg, sizeHelper, 4);
    }
}