package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;

import java.util.HashMap;
import java.util.Map;

public abstract class SizeHelper<T extends Instruction<T>, U extends Enum<U>> {
    public static final Map<String, Integer> stackSizes = new HashMap<String, Integer>();
    public static final Map<String, Long> maxValues = new HashMap<String, Long>();

    static {
        stackSizes.put(JoosNonTerminal.BYTE, 1);
        stackSizes.put(JoosNonTerminal.SHORT, 2);
        stackSizes.put(JoosNonTerminal.INTEGER, 4);
        stackSizes.put(JoosNonTerminal.LONG, 8);
        stackSizes.put(JoosNonTerminal.CHAR, 2);
        // Can't get 1 bit so just use 8.
        stackSizes.put(JoosNonTerminal.BOOLEAN, 1);

        maxValues.put(JoosNonTerminal.BYTE, (long) Byte.MAX_VALUE);
        maxValues.put(JoosNonTerminal.SHORT, (long) Short.MAX_VALUE);
        maxValues.put(JoosNonTerminal.INTEGER, (long) Integer.MAX_VALUE);
        maxValues.put(JoosNonTerminal.LONG, Long.MAX_VALUE);
        maxValues.put(JoosNonTerminal.CHAR, (long) Character.MAX_VALUE);
    }

    public boolean hasSetSize(String type) {
        return stackSizes.containsKey(type);
    }

    public int getByteSizeOfType(Typeable type) {
        return getByteSizeOfType(type.getType().getTypeDclNode().fullName);
    }

    public abstract int getByteSizeOfType(String typeName);

    public abstract int getBytePushSizeOfType(String typeName);

    public abstract U getSize(long stackSize);

    public abstract U getSizeOfType(String typeName);

    public abstract U getPushSize(U size);

    public abstract int getIntSize(U size);

    public final int getDefaultStackPower() {
        return 31 - Integer.numberOfLeadingZeros(getDefaultStackSize());
    }

    public abstract int getDefaultStackSize();

    public abstract int getMinSize();

    public abstract U getDefaultSize();

    public abstract U getCellSize();

    public abstract T[] alloceMinCellSpace(String s);

    public abstract T[] alloceDefaultCellSpace(String s);
}