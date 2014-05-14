package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.DataInstructionMaker;

public class X86SizeHelper extends SizeHelper<X86Instruction, Size> {
    public static final X86SizeHelper sizeHelper32 = new X86SizeHelper(false);
    public static final X86SizeHelper sizeHelper64 = new X86SizeHelper(true);

    private static final int MIN_BYTE_SIZE = 2;
    private final int defaultStackPower;
    private final int defaultStackSize;
    private final Size defaultStack;
    private final boolean use64;

    private X86SizeHelper(final boolean use64) {
        if (use64) {
            this.use64 = true;
            defaultStackPower = 3;
            defaultStackSize = 8;
            defaultStack = Size.QWORD;
        } else {
            this.use64 = false;
            defaultStackPower = 2;
            defaultStackSize = 4;
            defaultStack = Size.DWORD;
        }
    }

    @Override
    public int getByteSizeOfType(final String typeName) {
        return X86SizeHelper.stackSizes.containsKey(typeName) ? X86SizeHelper.stackSizes.get(typeName) : defaultStackSize;
    }

    @Override
    public int getBytePushSizeOfType(final String typeName) {
        int size = getByteSizeOfType(typeName);
        if (use64 && size == 4)
            size = 8;
        return size > MIN_BYTE_SIZE ? size : MIN_BYTE_SIZE;
    }

    @Override
    public Size getSize(final long stackSize) {
        if (stackSize == 8)
            return Size.QWORD;
        if (stackSize == 4)
            return Size.DWORD;
        if (stackSize == 2)
            return Size.WORD;
        if (stackSize == 1)
            return Size.LOW;
        throw new IllegalArgumentException("Nothing is of size " + stackSize);
    }

    @Override
    public final Size getSizeOfType(final String typeName) {
        return getSize(getByteSizeOfType(typeName));
    }

    @Override
    public final Size getPushSize(final Size size) {
        if (size == Size.HIGH || size == Size.LOW)
            return Size.WORD;
        if (use64 && size == Size.DWORD)
            return Size.QWORD;
        return size;
    }

    @Override
    public int getIntSize(final Size size) {
        switch (size) {
        case QWORD:
            return 8;
        case DWORD:
            return 4;
        case WORD:
            return 2;
        default:
            return 1;
        }
    }

    public static InstructionArg<X86Instruction, Size> getPowerSizeImd(final Size size) {
        switch (size) {
        case QWORD:
            return Immediate.QWORD_S;
        case DWORD:
            return Immediate.DWORD_S;
        case WORD:
            return Immediate.WORD_S;
        default:
            return Immediate.ZERO;
        }
    }

    public static String getStrVal(final Size size) {
        switch (size) {
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
    public final int getDefaultStackPower() {
        return defaultStackPower;
    }

    @Override
    public final int getDefaultStackSize() {
        return defaultStackSize;
    }

    @Override
    public final int getMinSize() {
        return MIN_BYTE_SIZE;
    }

    @Override
    public Size getDefaultSize() {
        return defaultStack;
    }

    @Override
    public Size getCellSize() {
        return Size.WORD;
    }

    @Override
    public X86Instruction[] alloceMinCellSpace(final String s) {
        final Immediate i = new Immediate(s);
        return DataInstructionMaker.make(i, getCellSize(), this);
    }

    @Override
    public X86Instruction[] alloceDefaultCellSpace(final String s) {
        final Immediate i = new Immediate(s);
        return DataInstructionMaker.make(i, getDefaultSize(), this);
    }
}
