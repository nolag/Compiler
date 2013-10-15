package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Neg extends UniInstruction{
    public Neg(final Memory what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("neg", what, sizeHelper, 3, 4);
    }

    public Neg(final Register what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("neg", what, sizeHelper, 1, 2);
    }
}
