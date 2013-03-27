package cs444.codegen.instructions;

import cs444.codegen.InstructionArg;

public class Jmp implements Instruction{
    private final InstructionArg arg1;

    public Jmp(InstructionArg arg1){
        this.arg1 = arg1;
    }

    @Override
    public String generate() {
        return "jmp " + arg1.getValue();
    }

}
