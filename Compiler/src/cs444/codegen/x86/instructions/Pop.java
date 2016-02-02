package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Pop extends UniInstruction {
    public Pop(final Register what, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("pop", what, sizeHelper, 4, 1);
    }

    public Pop(final Register what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("pop", what, sizeHelper, 4, 1);
    }

    public Pop(final Memory what, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("pop", what, sizeHelper, 6, 4);
    }

    public Pop(final Memory what, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("pop", what, sizeHelper, 6, 4);
    }
}
