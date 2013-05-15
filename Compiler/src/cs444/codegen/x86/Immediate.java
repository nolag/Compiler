package cs444.codegen.x86;



public class Immediate extends InstructionArg{
    private final String value;

    public static final Immediate ONE = new Immediate("1");
    public static final Immediate ZERO = new Immediate("0");
    public static final Immediate SOFTWARE_INTERUPT = new Immediate("80h");
    public static final Immediate PREP_EDX = new Immediate("31");
    public static final Immediate QWORD_I = new Immediate("8");
    public static final Immediate DWORD_I = new Immediate("4");
    public static final Immediate TWO = new Immediate("2");
    public static final Immediate QWORD_S = new Immediate("3");;
    public static final Immediate STACK_SIZE_POWER = new Immediate(String.valueOf(SizeHelper.DEFAULT_STACK_POWER));

    public static final Immediate C_TIMES_8 = new Immediate(Register.COUNTER, "*", 8);
    public static final Immediate C_TIMES_4 = new Immediate(Register.COUNTER, "*", 4);
    public static final Immediate C_TIMES_2 = new Immediate(Register.COUNTER, "*", 2);

    public static final Immediate NOTHING = new Immediate(";this should never actually be used, palce holder arg");

    //Same value as ONE
    public static final Immediate TRUE = ONE;
    public static final Immediate EXIT = ONE;
    public static final Immediate BYTE_I = ONE;
    public static final Immediate WORD_S = ONE;


    //same value as ONE
    public static final Immediate NULL = ZERO;
    public static final Immediate FALSE = ZERO;
    public static final Immediate BYTE_S = ZERO;

    //same value as TWO
    public static final Immediate WORD_I = TWO;
    public static final Immediate DWORD_S = TWO;

    public Immediate(final String value){
        this.value = value;
    }

    private Immediate(final InstructionArg arg, final String op, final int val){
        this(arg.getValue() + " " + op + " " + val);
    }

    public Immediate(final long value){
        this(String.valueOf(value));
    }

    public static Immediate getImediate(final Size size){
        switch(size){
        case QWORD: return QWORD_I;
        case DWORD: return DWORD_I;
        case WORD: return WORD_I;
        default: return BYTE_I;
        }
    }

    public static Immediate getImediateShift(final Size size){
        switch(size){
        case QWORD: return QWORD_S;
        case DWORD: return DWORD_S;
        case WORD: return WORD_S;
        default: return BYTE_S;
        }
    }

    @Override
    public String getValue(final Size size) {
        return value;
    }
}
