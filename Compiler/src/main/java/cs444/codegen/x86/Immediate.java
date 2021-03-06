package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

import java.util.HashMap;
import java.util.Map;

public class Immediate extends NotMemory {
    public static final Immediate ONE = new Immediate("1");
    public static final Immediate ZERO = new Immediate("0");
    public static final Immediate QWORD_I = new Immediate("8");
    public static final Immediate FOUR = new Immediate("4");
    public static final Immediate TWO = new Immediate("2");
    public static final Immediate QWORD_S = new Immediate("3");
    public static final Immediate THIRTY_ONE = new Immediate("31");
    public static final Immediate THIRTY_TWO = new Immediate("32");
    public static final Immediate SIXTY_THREE = new Immediate("63");
    public static final Immediate BIT_32 = new Immediate("0x80000000");
    public static final Immediate NOTHING = new Immediate(";this should never actually be used, palce holder arg");
    public static final Immediate NULL = new Immediate(Integer.toString(TileHelper.NULL));
    //Same value as ONE
    public static final Immediate TRUE = ONE;
    public static final Immediate BYTE_I = ONE;
    public static final Immediate WORD_S = ONE;
    public static final Immediate FALSE = ZERO;
    public static final Immediate BYTE_S = ZERO;
    //same value as TWO
    public static final Immediate WORD_I = TWO;
    public static final Immediate DWORD_S = TWO;
    //same value as FOUR
    public static final Immediate DWORD_I = FOUR;
    public static Map<SizeHelper<X86Instruction, ?>, Immediate> stackPowers = new HashMap<SizeHelper<X86Instruction,
            ?>, Immediate>();
    private final String value;

    public Immediate(String value) {
        this.value = value;
    }

    public Immediate(long value) {
        this(String.valueOf(value));
    }

    public static Immediate getImediate(Size size) {
        switch (size) {
            case QWORD:
                return QWORD_I;
            case DWORD:
                return DWORD_I;
            case WORD:
                return WORD_I;
            default:
                return BYTE_I;
        }
    }

    public static Immediate getImediateShift(Size size) {
        switch (size) {
            case QWORD:
                return QWORD_S;
            case DWORD:
                return DWORD_S;
            case WORD:
                return WORD_S;
            default:
                return BYTE_S;
        }
    }

    @Override
    public final String getValue(Size size, SizeHelper<X86Instruction, Size> sizeHelper) {
        return value;
    }

    public final Immediate getStackSizePower(SizeHelper<X86Instruction, ?> sizeHelper) {
        Immediate immediate = stackPowers.get(sizeHelper);
        if (immediate == null) {
            immediate = new Immediate(sizeHelper.getDefaultStackPower());
            stackPowers.put(sizeHelper, immediate);
        }
        return immediate;
    }

    @Override
    public final String toString() {
        return value;
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Immediate)) {
            return false;
        }
        Immediate imm = (Immediate) other;
        if (imm == this) {
            return true;
        }
        return value.equals(imm.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
