package cs444.codegen.instructions.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.SetInstruciton;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;


public class Setne extends SetInstruciton{
    public Setne(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("setne", arg, sizeHelper, 3);
    }

    public Setne(final Memory arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("setne", arg, sizeHelper, 4);
    }
}
