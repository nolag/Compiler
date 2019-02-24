package cs444.codegen.x86.instructions.factories;

import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Resb;
import cs444.codegen.x86.instructions.Resd;
import cs444.codegen.x86.instructions.Resq;
import cs444.codegen.x86.instructions.Resw;
import cs444.codegen.x86.instructions.bases.ReserveInstruction;

public class ReserveInstructionMaker {
    private ReserveInstructionMaker() { }

    public static ReserveInstruction make(String name, Size size) {
        switch (size) {
            case LOW:
            case HIGH:
                return new Resb(name, 1);
            case WORD:
                return new Resw(name, 1);
            case DWORD:
                return new Resd(name, 1);
            case QWORD:
                return new Resq(name, 1);
        }
        // shouldn't get here
        return null;
    }
}
