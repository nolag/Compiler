package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.SetInstruciton;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;


public class Sete extends SetInstruciton{
    public Sete(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("sete", arg, sizeHelper, 3);
    }

    public Sete(final Memory arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("sete", arg, sizeHelper, 4);
    }
}