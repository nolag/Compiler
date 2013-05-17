package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;


public class Ret implements X86Instruction {
    public static final Ret RET = new Ret();

    private Ret(){ }

    @Override
    public String generate() {
        return "ret";
    }

}
