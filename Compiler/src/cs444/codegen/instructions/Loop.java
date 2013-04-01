package cs444.codegen.instructions;

public class Loop implements Instruction {
    private final String s;

    public Loop(String s){
        this.s = s;
    }


    @Override
    public String generate() {
        return "loop " + s;
    }
}
