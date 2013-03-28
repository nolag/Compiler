package cs444.codegen.instructions;

public class Dd implements Instruction {
    private final String data;

    public Dd(String data) {
        this.data = data;
    }

    @Override
    public String generate() {
        return "dd " + data;
    }

}
