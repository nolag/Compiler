package cs444.codegen;

public class Immediate extends InstructionArg{
    private final String value;

    public static final Immediate EXIT = new Immediate("1");
    public static final Immediate SOFTWARE_INTERUPT = new Immediate("80h");

    public Immediate(String value){
        this.value = value;
    }

    @Override
    public String getValue(Size size) {
        return value;
    }

}
