package cs444.codegen.x86.instructions.factories;

import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Db;
import cs444.codegen.x86.instructions.Dd;
import cs444.codegen.x86.instructions.Dq;
import cs444.codegen.x86.instructions.Dw;
import cs444.codegen.x86.instructions.bases.UniInstruction;

public class DataInstructionMaker {
    private DataInstructionMaker() { }

    public static UniInstruction make(final Immediate data, final Size size) {
        switch (size){
        case LOW:
        case HIGH:
            return new Db(data);
        case WORD:
            return new Dw(data);
        case DWORD:
            return new Dd(data);
        case QWORD:
            return new Dq(data);
        }
        // shouldn't get here
        return null;
    }

}
