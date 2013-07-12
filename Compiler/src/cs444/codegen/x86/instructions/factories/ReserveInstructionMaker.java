package cs444.codegen.x86.instructions.factories;

import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Resb;
import cs444.codegen.x86.instructions.Resd;
import cs444.codegen.x86.instructions.Resq;
import cs444.codegen.x86.instructions.Resw;
import cs444.codegen.x86.instructions.bases.ReserveInstruction;

public class ReserveInstructionMaker {
    private ReserveInstructionMaker() { }

    public static ReserveInstruction make(final String name, final Size size, final long quantity) {
        switch (size){
        case LOW:
        case HIGH:
            return new Resb(name, quantity);
        case WORD:
            return new Resw(name, quantity);
        case DWORD:
            return new Resd(name, quantity);
        case QWORD:
            return new Resq(name, quantity);
        }
        // shouldn't get here
        return null;
    }

}
