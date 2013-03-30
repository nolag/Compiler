package cs444.codegen.instructions.factories;

import cs444.codegen.InstructionArg.Size;
import cs444.codegen.instructions.Resb;
import cs444.codegen.instructions.Resd;
import cs444.codegen.instructions.ReserveInstruction;
import cs444.codegen.instructions.Resw;

public class ReserveInstructionMaker {
    private ReserveInstructionMaker() { }

    public static ReserveInstruction make(String name, Size size, long quantity) {
        switch (size){
        case LOW:
        case HIGH:
            return new Resb(name, quantity);
        case WORD:
            return new Resw(name, quantity);
        case DWORD:
            return new Resd(name, quantity);
        }
        // shouldn't get here
        return null;
    }

}
