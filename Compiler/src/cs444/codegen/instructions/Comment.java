package cs444.codegen.instructions;

public class Comment implements Instruction{
    public final String comment;

    public Comment(String comment){
        this.comment = ";" + comment;
    }

    @Override
    public String generate() {
        return comment;
    }
}
