package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;


public class Db extends DataInstruction implements X86Instruction {
    public Db(InstructionArg data) {
        super(data);
    }

    @Override
    public String generate() {
        return "db " + data.getValue(Size.WORD);
    }
}
