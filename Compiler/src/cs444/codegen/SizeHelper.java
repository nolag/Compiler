package cs444.codegen;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.InstructionArg.Size;
import cs444.parser.symbols.JoosNonTerminal;

public class SizeHelper {
    // all sizes in bytes
    public static final int DEFAULT_STACK_SIZE = 4;
    public static final int MIN_STACK_SHIFT = 2;
    public static final int DEFAULT_STACK_POWER = 2;
    public static final Map<String, Integer> stackSizes = new HashMap<String, Integer>();

    static{
        SizeHelper.stackSizes.put(JoosNonTerminal.BYTE, 1);
        SizeHelper.stackSizes.put(JoosNonTerminal.SHORT, 2);
        SizeHelper.stackSizes.put(JoosNonTerminal.CHAR, 2);
        //Can't get 1 bit so just use 8.
        SizeHelper.stackSizes.put(JoosNonTerminal.BOOLEAN, 1);
    }

    public static int getByteSizeOfType(String typeName) {
        return SizeHelper.stackSizes.containsKey(typeName) ? SizeHelper.stackSizes.get(typeName) : DEFAULT_STACK_SIZE;
    }


    public static Size getSize(long stackSize) {
        if(stackSize == 2) return Size.WORD;
        if(stackSize == 1) return Size.LOW;
        return Size.DWORD;
    }

    public static Size getSizeOfType(String typeName) {
        return getSize(getByteSizeOfType(typeName));
    }

    public static Size getPushSize(Size size){
        if(size == Size.HIGH || size == Size.LOW) return Size.WORD;
        return size;
    }

    public static int getIntSize(Size size){
        switch(size){
        case DWORD: return 4;
        case WORD: return 2;
        default: return 1;
        }
    }

    public static InstructionArg getPowerSizeImd(Size size) {
        switch(size){
        case DWORD: return Immediate.TWO;
        case WORD: return Immediate.ONE;
        default: return Immediate.ZERO;
        }
    }

    public static InstructionArg getZeroImd(Size size){
        switch(size){
        case DWORD: return PointerRegister.ZEROING_REGISTER_4;
        case WORD: return PointerRegister.ZEROING_REGISTER_2;
        default: return PointerRegister.ZEROING_REGISTER_1;
        }
    }


    public static Size getBestZero(long size) {
        if(size % 4 == 0)  return Size.DWORD;
        if(size % 2 == 0)  return Size.WORD;
        return Size.HIGH;
    }
}
