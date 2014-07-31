package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Ldr;
import cs444.codegen.arm.instructions.Movt;
import cs444.codegen.arm.instructions.Movw;
import cs444.codegen.arm.instructions.Str;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class ArmSizeHelper extends SizeHelper<ArmInstruction, Size> {
    public static ArmSizeHelper sizeHelper32 = new ArmSizeHelper();

    private ArmSizeHelper() {}

    @Override
    public int getByteSizeOfType(String typeName) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getBytePushSizeOfType(String typeName) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Size getSize(long stackSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Size getSizeOfType(String typeName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Size getPushSize(Size size) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getIntSize(Size size) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getDefaultStackPower() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getDefaultStackSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMinSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Size getDefaultSize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Size getCellSize() {
        return Size.W;
    }

    @Override
    public ArmInstruction[] alloceMinCellSpace(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArmInstruction[] alloceDefaultCellSpace(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    public ArmInstruction makeCall(String what) {
        // TODO
        return null;
    }

    public ArmInstruction makeRet() {
        // TODO
        return null;
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
