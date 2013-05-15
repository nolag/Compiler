package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Resb;
import cs444.codegen.instructions.x86.Resd;
import cs444.codegen.instructions.x86.ReserveInstruction;
import cs444.codegen.instructions.x86.Resq;
import cs444.codegen.instructions.x86.Resw;
import cs444.codegen.x86.InstructionArg.Size;

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
