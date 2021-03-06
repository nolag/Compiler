package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;

public class ArmSizeHelper extends SizeHelper<ArmInstruction, Size> {
    private static final int MIN_BYTE_SIZE = 4;
    public static ArmSizeHelper sizeHelper32 = new ArmSizeHelper();
    private final int defaultStackSize;

    private ArmSizeHelper() {
        defaultStackSize = 4;
    }

    public static Size getSignedSize(Size size) {
        switch (size) {
            case B:
                return Size.SB;
            case H:
                return Size.SH;
            default:
                return size;
        }
    }

    public static byte getPowerSizeImd(Size size) {
        switch (size) {
            case D:
                return 3;
            case W:
                return 2;
            case SH:
            case H:
                return 1;
            default:
                return 0;
        }
    }

    public static ArmInstruction[] putInReg(Register reg, String loc, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        return new ArmInstruction[]{new Comment("Moving " + loc + "  ref into " + reg.getValue(sizeHelper)),
                new Movw(reg, new ImmediateStr(":lower16:" + loc), sizeHelper),
                new Movt(reg, new ImmediateStr(":upper16:" + loc), sizeHelper)};
    }

    public static ArmInstruction[] putInReg(Register reg, int val,
                                            SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new ArmInstruction[]{new Comment("Moving " + val + "  ref into " + reg.getValue(sizeHelper)),
                new Movw(reg, new Immediate16(val & 0x0000FFFF), sizeHelper), new Movt(reg,
                new Immediate16(val >>> 16), sizeHelper)};
    }

    public static ArmInstruction[] storeStatic(Register src, Register ref, String loc,
                                               SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new ArmInstruction[]{new Comment("Moving " + ref.getValue(sizeHelper) + " into " + loc),
                new Movw(ref, new ImmediateStr(":lower16:" + loc), sizeHelper),
                new Movt(ref, new ImmediateStr(":upper16:" + loc), sizeHelper), new Str(src, ref, sizeHelper)};
    }

    public static ArmInstruction[] storeStaticLong(Register src1, Register src2, Register ref,
                                                   String loc,
                                                   SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new ArmInstruction[]{new Comment("Moving long value into " + loc),
                new Movw(ref, new ImmediateStr(":lower16:" + loc), sizeHelper),
                new Movt(ref, new ImmediateStr(":upper16:" + loc), sizeHelper), new Str(src2, Register.R4,
                Immediate8.FOUR, sizeHelper),
                new Str(src1, Register.R4, sizeHelper)};
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
        if (stackSize == 8) {
            return Size.D;
        }
        if (stackSize == 4) {
            return Size.W;
        }
        if (stackSize == 2) {
            return Size.H;
        }
        if (stackSize == 1) {
            return Size.B;
        }
        throw new IllegalArgumentException("Nothing is of size " + stackSize);
    }

    @Override
    public Size getSizeOfType(String typeName) {
        return getSize(getByteSizeOfType(typeName));
    }

    @Override
    public Size getPushSize(Size size) {
        if (size == Size.D) {
            return Size.D;
        }
        return Size.W;
    }

    @Override
    public int getIntSize(Size size) {
        switch (size) {
            case D:
                return 8;
            case W:
                return 4;
            case SH:
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

    //TODO this and below was the bug, need align or something
    @Override
    public ArmInstruction[] alloceMinCellSpace(String s) {
        return new ArmInstruction[]{new Word(s)};
    }

    @Override
    public ArmInstruction[] alloceDefaultCellSpace(String s) {
        return new ArmInstruction[]{new Word(s)};
    }

    public ArmInstruction makeCall(String what) {
        return new Bl(what);
    }

    public ArmInstruction makeRet() {
        return ArmTileHelper.LEAVE;
    }
}
