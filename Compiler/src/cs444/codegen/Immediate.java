package cs444.codegen;

public class Immediate extends InstructionArg{
    private final String value;

    public static final Immediate TRUE = new Immediate("1");
    public static final Immediate FALSE = new Immediate("0");
    //Same value as true
    public static final Immediate EXIT = TRUE;
    //same value as false;
    public static final Immediate NULL = FALSE;
    public static final Immediate SOFTWARE_INTERUPT = new Immediate("80h");

    public Immediate(String value){
        this.value = value;
    }

    @Override
    public String getValue(Size size) {
        return value;
    }

}
