package cs444.codegen.arm.arm32;

import java.util.Set;

import cs444.codegen.Addable;
import cs444.codegen.ObjectLayout;
import cs444.codegen.OperatingSystem;
import cs444.codegen.Platform;
import cs444.codegen.arm.ArmPlatform;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.TileSet;
import cs444.types.APkgClassResolver;

public class Arm32Platform extends ArmPlatform {

    protected Arm32Platform(Set<String> options) {
        //TODO
        super(options, "a32", null, null, null);
    }

    @Override
    public ObjectLayout<ArmInstruction, Size> getObjectLayout() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OperatingSystem<? extends Platform<ArmInstruction, Size>>[] getOperatingSystems() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void genInstructorInvoke(APkgClassResolver resolver, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void genHeaderEnd(APkgClassResolver resolver, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public TileSet<ArmInstruction, Size> getTiles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TileHelper<ArmInstruction, Size> getTileHelper() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void zeroDefaultLocation(Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveStatic(String staticLbl, Size size, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void zeroStatic(String staticLbl, Size size, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveStaticLong(String staticLbl, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void zeroStaticLong(String staticLbl, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

}
