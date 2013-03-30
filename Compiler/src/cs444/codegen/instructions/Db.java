package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;


public class Db extends DataInstruction implements Instruction {
    public Db(InstructionArg data) {
        super(data);
    }

    @Override
    public String generate() {
        return "db " + data.getValue(Size.WORD);
    }
}
