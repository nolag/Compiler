package cs444.codegen.instructions.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;


public class Pop extends UniInstruction {
    public Pop(final Register what, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("pop", what, sizeHelper, 4);
    }

    public Pop(final Register what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("pop", what, sizeHelper, 4);
    }

    public Pop(final Memory what, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("pop", what, sizeHelper, 6);
    }

    public Pop(final Memory what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("pop", what, sizeHelper, 6);
    }
}
