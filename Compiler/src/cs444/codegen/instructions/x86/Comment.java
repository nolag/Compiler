package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;


public class Comment extends X86Instruction{
    public final String comment;

    public Comment(final String comment){
        super(0);
        this.comment = ";" + comment;
    }

    @Override
    public String generate() {
        return comment;
    }
}
