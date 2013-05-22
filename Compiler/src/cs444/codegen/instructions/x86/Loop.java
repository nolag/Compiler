package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;


public class Loop extends X86Instruction {
    private final String s;

    public Loop(final String s){
        //6 on loop 2 on no loop, assume most loop would round to 6
        super(6);
        this.s = s;
    }


    @Override
    public String generate() {
        return "loop " + s;
    }
}
