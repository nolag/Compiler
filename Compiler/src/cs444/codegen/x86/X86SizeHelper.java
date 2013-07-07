package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg.Size;

public class X86SizeHelper extends SizeHelper<X86Instruction>{
    public static final X86SizeHelper sizeHelper32 = new X86SizeHelper(false);
    public static final X86SizeHelper sizeHelper64 = new X86SizeHelper(true);
    public static final int MIN_BYTE_SIZE = 2;
    public static final Size SMALL_SIZE = Size.LOW;

    public final int defaultStackPower;
    public final int defaultStackSize;
    public final Size defaultStack;
    public final boolean use64;

    private X86SizeHelper(final boolean use64){
        this.use64 = use64;
        if(use64){
            defaultStackPower = 3;
            defaultStackSize = 8;
            defaultStack = Size.QWORD;
        }else{
            defaultStackPower = 2;
            defaultStackSize = 4;
            defaultStack = Size.DWORD;
        }
    }

    @Override
    public int getByteSizeOfType(final String typeName) {
        return X86SizeHelper.stackSizes.containsKey(typeName) ? X86SizeHelper.stackSizes.get(typeName) : defaultStackSize;
    }

    public static Size getSize(final long stackSize) {
        if(stackSize == 8) return Size.QWORD;
        if(stackSize == 4) return Size.DWORD;
        if(stackSize == 2) return Size.WORD;
        if(stackSize == 1) return Size.LOW;
        throw new IllegalArgumentException("Nothing is of size " + stackSize);
    }

    public final Size getSizeOfType(final String typeName) {
        return getSize(getByteSizeOfType(typeName));
    }

    public static final Size getPushSize(final Size size){
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

    public static String getStrVal(final Size size){
        switch(size){
        case LOW:
        case HIGH:
            return "byte";
        case WORD:
            return "word";
        case DWORD:
            return "dword";
        case QWORD:
            return "qword";
        default:
            return "ERROR!";
        }
    }

    @Override
    public final int getDefaultStackPower(){
        return defaultStackPower;
    }

    @Override
    public final int getDefaultStackSize(){
        return defaultStackSize;
    }

    @Override
    public final int getMinSize(){
        return MIN_BYTE_SIZE;
    }
}
