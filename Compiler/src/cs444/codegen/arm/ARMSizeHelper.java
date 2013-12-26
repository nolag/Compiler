package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.InstructionArg.Size;
import cs444.codegen.arm.instructions.bases.ARMInstruction;

public class ARMSizeHelper extends SizeHelper<ARMInstruction, Size> {

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
    public ARMInstruction allocDefaultSpace(String s) {
        // TODO Auto-generated method stub
        return null;
    }

}
