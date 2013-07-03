package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;


public class Ret extends X86Instruction {
    public static final Ret RET = new Ret();

    private Ret(){
        super(5);
    }

    @Override
    public String generate() {
        return "ret";
    }

}
