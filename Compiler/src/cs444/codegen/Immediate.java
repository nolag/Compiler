package cs444.codegen;

public class Immediate extends InstructionArg{
    private final String value;

    public static final Immediate TRUE = new Immediate("1");
    public static final Immediate FALSE = new Immediate("0");
    public static final Immediate SOFTWARE_INTERUPT = new Immediate("80h");
    public static final Immediate PREP_EDX = new Immediate("31");
    public static final Immediate DWORD_I = new Immediate("4");
    public static final Immediate WORD_I = new Immediate("2");
    public static final Immediate STACK_SIZE_POWER = new Immediate(String.valueOf(SizeHelper.DEFAULT_STACK_POWER));
    public static final Immediate C_TIMES_4 = new Immediate(Register.COUNTER, "*", SizeHelper.DEFAULT_STACK_SIZE);

    //Same value as true
    public static final Immediate EXIT = TRUE;
    public static final Immediate BYTE_I = TRUE;
    public static final Immediate WORD_S = TRUE;

    //same value as false;
    public static final Immediate NULL = FALSE;
    public static final Immediate ZERO = FALSE;
    public static final Immediate BYTE_S = FALSE;

    //same value as WORD_I
    public static final Immediate DWORD_S = new Immediate("2");

    public Immediate(String value){
        this.value = value;
    }

    private Immediate(InstructionArg arg, String op, int val){
        this(arg.getValue() + " " + op + " " + val);
    }

    public static Immediate getImediate(Size size){
        switch(size){
        case DWORD: return DWORD_I;
        case WORD: return WORD_I;
        default: return BYTE_I;
        }
    }

    public static Immediate getImediateShift(Size size){
        switch(size){
        case DWORD: return DWORD_S;
        case WORD: return WORD_S;
        default: return BYTE_S;
        }
    }

    @Override
    public String getValue(Size size) {
        return value;
    }

}
