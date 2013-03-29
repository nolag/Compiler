package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;


public class Dw extends DataInstruction implements Instruction {
    public Dw(InstructionArg data) {
        super(data);
    }

    @Override
    public String generate() {
        return "dw " + data.getValue(Size.LOW);
    }

}
