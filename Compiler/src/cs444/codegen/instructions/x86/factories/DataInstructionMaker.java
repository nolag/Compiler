package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Db;
import cs444.codegen.instructions.x86.Dd;
import cs444.codegen.instructions.x86.Dq;
import cs444.codegen.instructions.x86.Dw;
import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;

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
