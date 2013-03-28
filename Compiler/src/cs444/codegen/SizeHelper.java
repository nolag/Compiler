package cs444.codegen;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.InstructionArg.Size;
import cs444.parser.symbols.JoosNonTerminal;

public class SizeHelper {

    public static final int DEFAULT_STACK_SIZE = 32;
    public static final int MIN_STACK_SHIFT = 16;
    public static final Map<String, Integer> stackSizes = new HashMap<String, Integer>();

    static{
        SizeHelper.stackSizes.put(JoosNonTerminal.BYTE, 8);
        SizeHelper.stackSizes.put(JoosNonTerminal.SHORT, 16);
        SizeHelper.stackSizes.put(JoosNonTerminal.CHAR, 16);
        //Can't get 1 bit so just use 8.
        SizeHelper.stackSizes.put(JoosNonTerminal.BOOLEAN, 8);
    }

    public static int getBitSizeOfType(String typeName) {
        return SizeHelper.stackSizes.containsKey(typeName) ? SizeHelper.stackSizes.get(typeName) : DEFAULT_STACK_SIZE;
    }


    public static Size getSize(int stackSize) {
        if(stackSize == 16) return Size.WORD;
        if(stackSize == 8) return Size.LOW;
        return Size.DWORD;
    }

    public static Size getSizeOfType(String typeName) {
        return getSize(getBitSizeOfType(typeName));
    }
}
