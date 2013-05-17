package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;


public class Loop implements X86Instruction {
    private final String s;

    public Loop(String s){
        this.s = s;
    }


    @Override
    public String generate() {
        return "loop " + s;
    }
}
