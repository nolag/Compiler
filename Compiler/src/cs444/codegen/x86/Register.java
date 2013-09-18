package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Register extends NotMemory {
    public static final Register ACCUMULATOR = new Register("a", 'x');
    public static final Register COUNTER = new Register("c", 'x');
    public static final Register DATA = new Register("d", 'x');
    public static final Register BASE = new Register("b", 'x');
    public static final Register STACK = new Register("s", 'p');
    public static final Register FRAME = new Register("b", 'p');
    public static final Register SOURCE = new Register("s", 'i');
    public static final Register DESTINATION = new Register("d", 'i');

    protected final String name;
    private final char ending;

    protected Register(final String name, final char ending) {
        this.name = name;
        this.ending = ending;
    }

    public String get8High() {
        return name + 'h';
    }

    public String get8Low() {
        return name + 'l';
    }

    public String get16() {
        return name + ending;
    }

    public String get32() {
        return 'e' + name + ending;
    }

    public String get64() {
        return 'r' + name + ending;
    }

    @Override
    public String getValue(final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        switch(size){
        case LOW: return get8Low();
        case HIGH: return get8High();
        case WORD: return get16();
        case DWORD: return get32();
        case QWORD: return get64();
        }
        return null;
    }
}
