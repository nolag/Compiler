package cs444.codegen;


public class Register extends InstructionArg{

    public static final Register ACCUMULATOR = new Register("a", 'x');
    public static final Register COUNTER = new Register("c", 'x');
    public static final Register DATA = new Register("d", 'x');
    public static final Register BASE = new Register("b", 'x');
    public static final Register STACK = new Register("s", 'p');
    public static final Register FRAME = new Register("b", 'p');
    public static final Register SOURCE = new Register("s", 'i');
    public static final Register DESTINATION = new Register("d", 'i');

    private final String name;
    private final char ending;

    private Register(String name, char ending) {
        this.name = name;
        this.ending = ending;
    }

    public String get8High(){
        return name + 'h';
    }

    public String get8Low(){
        return name + 'l';
    }

    public String get16(){
        return name + ending;
    }

    public String get32(){
        return 'e' + name + ending;
    }

    @Override
    public String getValue(Size size) {
        switch(size){
        case LOW: return get8Low();
        case HIGH: return get8High();
        case WORD: return get16();
        case DWORD: return get32();
        }
        return null;
    }
}
