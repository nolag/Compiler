package cs444.codegen.x86;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.x86.InstructionArg.Size;
import cs444.parser.symbols.JoosNonTerminal;

public class SizeHelper {
    // all sizes in bytes
    //TODO do this for x86 and for x64
    public static final int DEFAULT_STACK_SIZE = 4;
    public static final int MIN_STACK_SHIFT = 2;
    public static final int DEFAULT_STACK_POWER = 2;

    public static final Size DEFAULT_STACK = getSize(DEFAULT_STACK_SIZE);

    public static final Map<String, Integer> stackSizes = new HashMap<String, Integer>();

    static{
        SizeHelper.stackSizes.put(JoosNonTerminal.BYTE, 1);
        SizeHelper.stackSizes.put(JoosNonTerminal.SHORT, 2);
        SizeHelper.stackSizes.put(JoosNonTerminal.CHAR, 2);
        //Can't get 1 bit so just use 8.
        SizeHelper.stackSizes.put(JoosNonTerminal.BOOLEAN, 1);
    }

    public static int getByteSizeOfType(final String typeName) {
        return SizeHelper.stackSizes.containsKey(typeName) ? SizeHelper.stackSizes.get(typeName) : DEFAULT_STACK_SIZE;
    }


    public static Size getSize(final long stackSize) {
        if(stackSize == 8) return Size.QWORD;
        if(stackSize == 4) return Size.DWORD;
        if(stackSize == 2) return Size.WORD;
        if(stackSize == 1) return Size.LOW;
        throw new IllegalArgumentException("Nothing is of size " + stackSize);
    }

    public static Size getSizeOfType(final String typeName) {
        return getSize(getByteSizeOfType(typeName));
    }

    public static Size getPushSize(final Size size){
        if(size == Size.HIGH || size == Size.LOW) return Size.WORD;
        return size;
    }

    public static int getIntSize(final Size size){
        switch(size){
        case QWORD: return 8;
        case DWORD: return 4;
        case WORD: return 2;
        default: return 1;
        }
    }

    public static InstructionArg getPowerSizeImd(final Size size) {
        switch(size){
        case QWORD: return Immediate.QWORD_S;
        case DWORD: return Immediate.DWORD_S;
        case WORD: return Immediate.WORD_S;
        default: return Immediate.ZERO;
        }
    }

    public static InstructionArg getZeroImd(final Size size){
        switch(size){
        case QWORD: return PointerRegister.ZEROING_REGISTER_8;
        case DWORD: return PointerRegister.ZEROING_REGISTER_4;
        case WORD: return PointerRegister.ZEROING_REGISTER_2;
        default: return PointerRegister.ZEROING_REGISTER_1;
        }
    }

    //TODO make this different for x64, add the commented line
    public static Size getBestZero(final long size) {
        //if(size % 8 == 0)  return Size.QWORD;
        if(size % 4 == 0)  return Size.DWORD;
        if(size % 2 == 0)  return Size.WORD;
        return Size.HIGH;
    }
}
