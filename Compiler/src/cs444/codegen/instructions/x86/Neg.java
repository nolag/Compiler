package cs444.codegen.instructions.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;


public class Neg extends UniInstruction{
    public Neg(final Memory what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("neg", what, sizeHelper, 3);
    }

    public Neg(final Register what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("neg", what, sizeHelper, 1);
    }
}
