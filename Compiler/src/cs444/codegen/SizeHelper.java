package cs444.codegen;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.JoosNonTerminal;

public abstract class SizeHelper<T extends Instruction> {
    public static final Map<String, Integer> stackSizes = new HashMap<String, Integer>();
    public static final Map<String, Long> maxValues = new HashMap<String, Long>();

    static{
        stackSizes.put(JoosNonTerminal.BYTE, 1);
        stackSizes.put(JoosNonTerminal.SHORT, 2);
        stackSizes.put(JoosNonTerminal.INTEGER, 4);
        stackSizes.put(JoosNonTerminal.CHAR, 2);
        //Can't get 1 bit so just use 8.
        stackSizes.put(JoosNonTerminal.BOOLEAN, 1);

        maxValues.put(JoosNonTerminal.BYTE, (long)Byte.MAX_VALUE);
        maxValues.put(JoosNonTerminal.SHORT, (long)Short.MAX_VALUE);
        maxValues.put(JoosNonTerminal.INTEGER, (long)Integer.MAX_VALUE);
        maxValues.put(JoosNonTerminal.CHAR, (long)Character.MAX_VALUE);
    }

    public abstract int getByteSizeOfType(String typeName);
    public abstract int getDefaultStackPower();
    public abstract int getDefaultStackSize();
    public abstract int getMinSize();
}