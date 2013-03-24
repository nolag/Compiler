package cs444.codegen;

public class Immediate extends InstructionArg{
    private final String value;

    public Immediate(String value){
        this.value = value;
    }

    @Override
    public String getValue(Size size) {
        return value;
    }

}
