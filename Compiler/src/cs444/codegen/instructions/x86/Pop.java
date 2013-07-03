package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Pop extends UniInstruction {
    public Pop(final Register what, final Size size, final X86SizeHelper sizeHelper){
        super("pop", what, sizeHelper, 4);
    }

    public Pop(final Register what, final X86SizeHelper sizeHelper){
        super("pop", what, sizeHelper, 4);
    }

    public Pop(final Memory what, final Size size, final X86SizeHelper sizeHelper){
        super("pop", what, sizeHelper, 6);
    }

    public Pop(final Memory what, final X86SizeHelper sizeHelper){
        super("pop", what, sizeHelper, 6);
    }
}
