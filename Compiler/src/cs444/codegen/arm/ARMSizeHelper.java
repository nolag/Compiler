package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.Bl;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Label;
import cs444.codegen.arm.instructions.Ldr;
import cs444.codegen.arm.instructions.Movt;
import cs444.codegen.arm.instructions.Movw;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Str;
import cs444.codegen.arm.instructions.Word;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class ArmSizeHelper extends SizeHelper<ArmInstruction, Size> {
    public static ArmSizeHelper sizeHelper32 = new ArmSizeHelper();

    private static final int MIN_BYTE_SIZE = 2;

    private final int defaultStackSize;

    private ArmSizeHelper() {
        defaultStackSize = 4;
    }

    @Override
    public int getByteSizeOfType(String typeName) {
        return stackSizes.containsKey(typeName) ? stackSizes.get(typeName) : defaultStackSize;
    }

    @Override
    public int getBytePushSizeOfType(String typeName) {
        int size = getByteSizeOfType(typeName);
        return size > MIN_BYTE_SIZE ? size : MIN_BYTE_SIZE;
    }

    @Override
    public Size getSize(long stackSize) {
        if (stackSize == 8) return Size.D;
        if (stackSize == 4) return Size.W;
        if (stackSize == 2) return Size.H;
        if (stackSize == 1) return Size.B;
        throw new IllegalArgumentException("Nothing is of size " + stackSize);
    }

    @Override
    public Size getSizeOfType(String typeName) {
        return getSize(getByteSizeOfType(typeName));
    }

    @Override
    public Size getPushSize(Size size) {
        if (size == Size.D) return Size.D;
        return Size.W;
    }

    @Override
    public int getIntSize(Size size) {
        switch (size) {
        case D:
            return 8;
        case W:
            return 4;
        case H:
            return 2;
        default:
            return 1;
        }
    }

    @Override
    public int getDefaultStackSize() {
        return defaultStackSize;
    }

    @Override
    public int getMinSize() {
        return MIN_BYTE_SIZE;
    }

    @Override
    public Size getDefaultSize() {
        return Size.W;
    }

    @Override
    public Size getCellSize() {
        return Size.W;
    }

    @Override
    public ArmInstruction[] alloceMinCellSpace(String s) {
        return new ArmInstruction[] { new Label(s), new Word(0) };
    }

    @Override
    public ArmInstruction[] alloceDefaultCellSpace(String s) {
        return new ArmInstruction[] { new Label(s), new Word(0) };
    }

    public ArmInstruction makeCall(String what) {
        return new Bl(new ImmediateStr(what));
    }

    public ArmInstruction makeRet() {
        return new Pop(Register.INTRA_PROCEDURE, Register.PC);
    }

    public static ArmInstruction[] putInReg(final Register reg, final String loc, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new ArmInstruction[] { new Comment("Moving " + loc + "  ref into " + reg.getValue(sizeHelper)),
                new Movw(reg, new ImmediateStr(":lower16:" + loc), sizeHelper),
                new Movt(reg, new ImmediateStr(":upper16:" + loc), sizeHelper) };
    }

    public static ArmInstruction[] putInReg(final Register reg, final int val, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new ArmInstruction[] { new Comment("Moving " + val + "  ref into " + reg.getValue(sizeHelper)),
                new Movw(reg, new Immediate16(val), sizeHelper), new Movt(reg, new Immediate16(val >> 16), sizeHelper) };
    }

    public static ArmInstruction[] loadStatic(final Register dest, final Register ref, final String loc,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new ArmInstruction[] { new Comment("Moving " + loc + " into " + ref.getValue(sizeHelper)),
                new Movw(ref, new ImmediateStr(":lower16:" + loc), sizeHelper),
                new Movt(ref, new ImmediateStr(":upper16:" + loc), sizeHelper), new Ldr(dest, ref, sizeHelper) };
    }

    public static ArmInstruction[] loadStatic(final Register dest, final String loc, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        return loadStatic(dest, dest, loc, sizeHelper);
    }

    public static ArmInstruction[] storeStatic(final Register src, final Register ref, final String loc,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new ArmInstruction[] { new Comment("Moving " + ref.getValue(sizeHelper) + " into " + loc),
                new Movw(ref, new ImmediateStr(":lower16:" + loc), sizeHelper),
                new Movt(ref, new ImmediateStr(":upper16:" + loc), sizeHelper), new Str(src, ref, sizeHelper) };
    }

    public static ArmInstruction[] storeStatic(final Register src, final String loc, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        return storeStatic(src, src, loc, sizeHelper);
    }
}
