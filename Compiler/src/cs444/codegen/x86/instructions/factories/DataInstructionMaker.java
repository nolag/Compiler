package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Db;
import cs444.codegen.x86.instructions.Dd;
import cs444.codegen.x86.instructions.Dq;
import cs444.codegen.x86.instructions.Dw;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class DataInstructionMaker {
    private DataInstructionMaker() {}

    public static UniInstruction[] make(final Immediate data, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        switch (size) {
        case LOW:
        case HIGH:
            return new UniInstruction[] { new Db(data) };
        case WORD:
            return new UniInstruction[] { new Dw(data) };
        case DWORD:
            return new UniInstruction[] { new Dd(data) };
        case QWORD:
            if (sizeHelper.getDefaultStackSize() < sizeHelper.getIntSize(Size.QWORD)) { return new UniInstruction[] { new Dd(data),
                    new Dd(data) }; }
            return new UniInstruction[] { new Dq(data) };
        }
        // shouldn't get here
        return null;
    }

}
