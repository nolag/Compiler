package cs444.codegen.instructions.x86;


public class Comment implements X86Instruction{
    public final String comment;

    public Comment(String comment){
        this.comment = ";" + comment;
    }

    @Override
    public String generate() {
        return comment;
    }
}
