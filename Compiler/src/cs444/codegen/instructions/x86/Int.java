package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;

public class Int implements X86Instruction {
    private final InstructionArg arg;

    public Int(InstructionArg arg){
        this.arg = arg;
    }

    @Override
    public String generate() {
        return "int " + arg.getValue();
    }

}
