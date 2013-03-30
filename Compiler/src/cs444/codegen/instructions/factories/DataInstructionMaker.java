package cs444.codegen.instructions.factories;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;
import cs444.codegen.instructions.DataInstruction;
import cs444.codegen.instructions.Db;
import cs444.codegen.instructions.Dd;
import cs444.codegen.instructions.Dw;

public class DataInstructionMaker {
    private DataInstructionMaker() { }

    public static DataInstruction make(InstructionArg data, Size size) {
        switch (size){
        case LOW:
        case HIGH:
            return new Db(data);
        case WORD:
            return new Dw(data);
        case DWORD:
            return new Dd(data);
        }
        // shouldn't get here
        return null;
    }

}
