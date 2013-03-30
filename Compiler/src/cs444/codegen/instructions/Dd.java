package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;
import cs444.codegen.InstructionArg.Size;


public class Dd extends DataInstruction implements Instruction {
    public Dd(InstructionArg data) {
        super(data);
    }

    @Override
    public String generate() {
        return "dd " + data.getValue(Size.DWORD);
    }

}
